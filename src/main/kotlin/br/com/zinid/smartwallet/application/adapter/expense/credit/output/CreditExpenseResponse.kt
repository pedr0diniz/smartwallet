package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreditCardInstallmentsResponse
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import java.math.BigDecimal
import java.time.LocalDate

data class CreditExpenseResponse(
    val id: Long? = null,
    val content: String,
    val date: LocalDate,
    val price: BigDecimal,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,
    val tag: String? = null,
    val creditCardInstallments: CreditCardInstallmentsResponse?
) {
    companion object {
        fun fromDomain(creditExpense: CreditExpense): CreditExpenseResponse =
            CreditExpenseResponse(
                id = creditExpense.id,
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
