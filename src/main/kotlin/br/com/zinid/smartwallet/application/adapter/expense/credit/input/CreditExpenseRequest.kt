package br.com.zinid.smartwallet.application.adapter.expense.credit.input

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreditExpenseRequest(
    @field:NotBlank
    val content: String,

    val date: LocalDate?,

    @field:Positive
    @field:NotNull
    val price: BigDecimal,

    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,

    val tag: String? = null,

    @field:Positive
    @field:NotNull
    val creditCardId: Long,

    @field:Positive
    val numberOfInstallments: Int?
) {
    fun toDomain() = CreditExpense(
        id = null,
        content = content,
        date = date ?: LocalDate.now(),
        price = price,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = CreditCard.createBlankFromId(creditCardId),
        numberOfInstallments = numberOfInstallments
    )
}
