package br.com.zinid.smartwallet.application.adapter.expense.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseRequest(
    @field:NotBlank
    @field:Size(max = 255)
    val content: String,

    @field:NotBlank
    @field:JsonFormat(pattern = "YYYY-MM-DD")
    val date: String,

    @field:NotNull
    val price: BigDecimal,

    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,

    @field:NotNull
    @field:Positive
    val paymentMethodId: Long,

    @field:Min(2)
    val numberOfInstallments: Int?
) {
    fun toDomain() = Expense(
        content = content,
        date = LocalDate.parse(date),
        price = price,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = PaymentMethod.createBlankFromId(id = paymentMethodId),
        creditCardInstallments = if (numberOfInstallments != null) {
            CreditCardInstallments.createBlankFromNumberOfInstallments(numberOfInstallments)
        } else {
            null
        }
    )
}
