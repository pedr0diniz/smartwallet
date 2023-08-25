package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreditCardInstallmentsResponse
import br.com.zinid.smartwallet.application.adapter.expense.ExpenseResponse
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import java.math.BigDecimal
import java.time.LocalDate

data class CreditExpenseResponse(
    override val id: Long? = null,
    override val content: String,
    override val date: LocalDate,
    override val price: BigDecimal,
    override val essential: Boolean? = false,
    override val monthlySubscription: Boolean? = false,
    override val tag: String? = null,
    val creditCardInstallments: CreditCardInstallmentsResponse?
) : ExpenseResponse {
    companion object {
        fun fromDomain(creditExpense: CreditExpense): CreditExpenseResponse =
            CreditExpenseResponse(
                id = creditExpense.id.let { if (it != 0L) it else null },
                content = creditExpense.content,
                date = creditExpense.date,
                price = creditExpense.price,
                essential = creditExpense.essential,
                monthlySubscription = creditExpense.monthlySubscription,
                tag = creditExpense.tag,
                creditCardInstallments = creditExpense.creditCardInstallments?.let {
                    CreditCardInstallmentsResponse.fromDomain(it)
                }
            )
    }
}
