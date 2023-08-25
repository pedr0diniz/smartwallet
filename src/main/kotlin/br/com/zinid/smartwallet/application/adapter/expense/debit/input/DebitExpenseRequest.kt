package br.com.zinid.smartwallet.application.adapter.expense.debit.input

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class DebitExpenseRequest(
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
    val debitPaymentMethodId: Long,
) {
    fun toDomain() = DebitExpense(
        id = null,
        content = content,
        date = date ?: LocalDate.now(),
        price = price,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = DebitPaymentMethod.createBlankFromId(debitPaymentMethodId)
    )
}
