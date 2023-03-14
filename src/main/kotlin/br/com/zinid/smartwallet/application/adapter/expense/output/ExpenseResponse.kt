package br.com.zinid.smartwallet.application.adapter.expense.output

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreditCardInstallmentsResponse
import br.com.zinid.smartwallet.domain.expense.Expense
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseResponse(
    val id: Long? = null,
    val content: String,
    val date: LocalDate,
    val price: BigDecimal,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,
    val creditCardInstallments: CreditCardInstallmentsResponse? = null
) {
    companion object {
        fun fromDomain(expense: Expense) = ExpenseResponse(
            id = expense.id,
            content = expense.content,
            date = expense.date,
            price = expense.price,
            essential = expense.essential,
            monthlySubscription = expense.monthlySubscription,
            creditCardInstallments = if (expense.creditCardInstallments != null) {
                CreditCardInstallmentsResponse.fromDomain(expense.creditCardInstallments)
            } else {
                null
            }
        )
    }
}