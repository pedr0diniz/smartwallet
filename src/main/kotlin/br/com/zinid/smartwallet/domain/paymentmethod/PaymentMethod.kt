package br.com.zinid.smartwallet.domain.paymentmethod

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import java.math.BigDecimal
import java.time.LocalDate

data class PaymentMethod(
    val id: Long? = null,
    val method: PaymentMethods,
    var creditCard: CreditCard? = null,
    val financialAccount: FinancialAccount,
    val expenses: List<Expense>? = listOf()
) {
    fun isCredit() = (method == PaymentMethods.CREDIT) && (creditCard != null)

    fun hasCreditCardLimit(expenseValue: BigDecimal): Boolean {
        if (isCredit()) {
            return creditCard?.hasLimit(
                getMonthlyExpensesValue().add(getOngoingInstallmentsValue()),
                expenseValue
            )!!
        }
        return false
    }

    private fun getMonthlyExpensesValue() = getNonInstallmentCreditExpensesWithinDateRange(
        creditCard!!.previousInvoiceClosingDate,
        creditCard!!.currentInvoiceClosingDate
    ).sumOf { it.price }

    private fun getNonInstallmentCreditExpensesWithinDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ) = expenses
        ?.filter { it.isCreditPurchase() }
        ?.filter { it.wasPurchasedWithinDateRange(startDate, endDate) }
        ?.filter { it.creditCardInstallments == null }
        ?: listOf()

    private fun getOngoingInstallmentsValue() = expenses?.sumOf {
        it.creditCardInstallments
            ?.getOngoingInstallmentsValue(creditCard!!.previousInvoiceClosingDate)
            ?: BigDecimal.ZERO
    }!!

    companion object {

        fun createBlank() = PaymentMethod(
            id = 0L,
            method = PaymentMethods.BLANK,
            financialAccount = FinancialAccount.createBlank()
        )
        fun createBlankFromId(id: Long) = PaymentMethod(
            id = id,
            method = PaymentMethods.BLANK,
            financialAccount = FinancialAccount.createBlank()
        )
    }
}
