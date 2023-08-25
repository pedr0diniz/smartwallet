package br.com.zinid.smartwallet.application.adapter.expense

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseResponse
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseResponse
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.financialaccount.input.FindFinancialAccountInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.FindCreditCardInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.FindDebitPaymentMethodInputPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val findFinancialAccountUseCase: FindFinancialAccountInputPort,
    private val findCreditCardUseCase: FindCreditCardInputPort,
    private val findDebitPaymentMethodUseCase: FindDebitPaymentMethodInputPort
) {

    @GetMapping("{userId}/monthlyExpenses/v1")
    fun getMonthlyExpensesV1(@PathVariable userId: Long): ResponseEntity<Any?> {
        val financialAccounts = findFinancialAccountUseCase.findByUserId(userId)
        val creditCards = financialAccounts.map { findCreditCardUseCase.findByFinancialAccountId(it.id!!) }.flatten()
        val debitPaymentMethods =
            financialAccounts.map { findDebitPaymentMethodUseCase.findByFinancialAccountId(it.id!!) }.flatten()
        val creditExpenses = creditCards.map { it.getMonthlyExpenses() }.flatten()
        val debitExpenses = debitPaymentMethods.map { it.getMonthlyExpenses() }.flatten()

        val expenseResponse =
            creditExpenses.map { CreditExpenseResponse.fromDomain(it as CreditExpense) } +
                debitExpenses.map { DebitExpenseResponse.fromDomain(it) }

        return ResponseEntity.ok(expenseResponse)
    }
}
