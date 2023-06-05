package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import java.math.BigDecimal
import java.time.LocalDate

data class DebitExpenseResponse(
    val id: Long? = null,
    val content: String,
    val date: LocalDate,
    val price: BigDecimal,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,
) {
    companion object {
        fun fromDomain(debitExpense: DebitExpense) = DebitExpenseResponse(
            id = debitExpense.id,
            content = debitExpense.content,
            date = debitExpense.date,
            price = debitExpense.price,
            essential = debitExpense.essential,
            monthlySubscription = debitExpense.monthlySubscription
        )
    }
}
