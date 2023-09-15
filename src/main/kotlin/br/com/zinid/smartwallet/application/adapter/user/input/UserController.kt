package br.com.zinid.smartwallet.application.adapter.user.input

import br.com.zinid.smartwallet.application.adapter.expense.ExpenseResponse
import br.com.zinid.smartwallet.application.adapter.expense.MonthlyExpensesResponse
import br.com.zinid.smartwallet.application.adapter.user.output.UserResponse
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.user.input.CreateUserInputPort
import br.com.zinid.smartwallet.domain.user.input.FindUserInputPort
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

@RestController
@RequestMapping("/users")
class UserController(
    private val createUserUseCase: CreateUserInputPort,
    private val findUserUseCase: FindUserInputPort
) {

    @PostMapping
    fun create(@Valid @RequestBody userRequest: UserRequest): ResponseEntity<Any?> {
        val possibleUser = createUserUseCase.execute(userRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(possibleUser))
    }

    @GetMapping("{userId}/monthlyExpenses")
    fun getMonthlyExpensesByUserId(
        @PathVariable userId: Long,
        @Parameter(
            name = "query_params",
            `in` = ParameterIn.QUERY,
            required = false,
            schema = Schema(
                type = "object",
                additionalProperties = Schema.AdditionalPropertiesValue.TRUE, ref = "#/components/schemas/ParameterMap"),
            style = ParameterStyle.FORM,
            explode = Explode.TRUE
            )
        @RequestParam queryParameters: Map<String, String>
    ): ResponseEntity<Any?> {
        val user = findUserUseCase.findById(userId) ?: return ResponseEntity.notFound().build() // TODO - error handling
        val expensesResponse = filterMonthlyExpenses(user.financialAccounts, queryParameters)

        return ResponseEntity.ok(expensesResponse)
    }

    private fun filterMonthlyExpenses(
        financialAccounts: List<FinancialAccount>,
        queryParameters: Map<String, String>
    ): MonthlyExpensesResponse {
        val paymentType = queryParameters["payment_type"]
        val yearMonth = try {
            YearMonth.parse(queryParameters["year_month"])
        } catch (ex: Exception) {
            YearMonth.now()
        }

        val expensesResponse = financialAccounts
            .asSequence()
            .map { filterPaymentMethods(it.paymentMethods, paymentType) }.flatten()
            .map { filterExpenses(it.getMonthlyExpenses(yearMonth), queryParameters) }.flatten()
            .map { ExpenseResponse.fromDomain(it) }
            .toList()

        return MonthlyExpensesResponse.fromExpenseList(expensesResponse, yearMonth)
    }

    private fun filterPaymentMethods(paymentMethods: List<PaymentMethod>, paymentType: String?): List<PaymentMethod> {
        if (paymentType == null) return paymentMethods
        return try {
            val parsedType = PaymentType.valueOf(paymentType.uppercase())

            if (parsedType == PaymentType.ALL_BUT_CREDIT) paymentMethods.filterNot { it.type == PaymentType.CREDIT }
            else paymentMethods.filter { it.type == parsedType }
        } catch (ex: Exception) {
            println("Failed to parse [$paymentType] as PaymentType")
            paymentMethods
        }
    }

    private fun filterExpenses(expenses: List<Expense>, queryParameters: Map<String, String>): List<Expense> {
        val essential = queryParameters["essential"]
        val monthlySubscription = queryParameters["monthly_subscription"]
        val installmentsOnly = queryParameters["installments_only"].toBoolean()
        val tags = queryParameters["tags"]?.uppercase()?.split(",")

        val expensesByFlags = expenses.apply {
            if (essential != null) this.filter { it.essential == essential.toBoolean() }
            if (monthlySubscription != null) this.filter { it.monthlySubscription == monthlySubscription.toBoolean() }
        }

        val expensesByFlagsAndInstallmentFlag = if (installmentsOnly) expensesByFlags.filter { it.id == 0L }
        else expensesByFlags

        return if (tags.isNullOrEmpty()) expensesByFlagsAndInstallmentFlag
        else expensesByFlagsAndInstallmentFlag.filter { tags.contains(it.tag) }
    }
}
