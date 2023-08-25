package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import br.com.zinid.smartwallet.application.adapter.expense.ExpenseResponse
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import java.math.BigDecimal
import java.time.LocalDate

data class DebitExpenseResponse(
    override val id: Long? = null,
    override val content: String,
    override val date: LocalDate,
    override val price: BigDecimal,
    override val essential: Boolean? = false,
    override val monthlySubscription: Boolean? = false,
    override val tag: String? = null
) : ExpenseResponse {
    companion object {
        fun fromDomain(debitExpense: DebitExpense) = DebitExpenseResponse(
            id = debitExpense.id.let { if (it != 0L) it else null },
            content = debitExpense.content,
            date = debitExpense.date,
            price = debitExpense.price,
            essential = debitExpense.essential,
            monthlySubscription = debitExpense.monthlySubscription,
            tag = debitExpense.tag
        )
    }
}
