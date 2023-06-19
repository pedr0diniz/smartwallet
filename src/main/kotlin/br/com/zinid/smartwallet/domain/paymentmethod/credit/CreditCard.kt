package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.filterWithinDateRange
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsAsExpenses
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsValue
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.utils.DateHelper.getClosingDateWithValidDay
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCard(
    override val id: Long? = null,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    override val financialAccount: FinancialAccount,
    override val expenses: List<CreditExpense>? = listOf(),
    val invoiceClosingDayOfMonth: Int // TODO - allow users to add the due date of their card rather than the closing day
//    val dueDayOfMonth: Int
) : PaymentMethod {

    override val type: PaymentType = PaymentType.CREDIT

    val currentInvoiceClosingDate: LocalDate = getCurrentClosingDate(invoiceClosingDayOfMonth)
    val previousInvoiceClosingDate: LocalDate = getPreviousClosingDate(invoiceClosingDayOfMonth)

    override fun getRemainingSpendableValue(): BigDecimal = cardLimit
        .minus(getMonthlyExpensesValue())
        .minus(getOngoingInstallmentsValue())
        .add(getCurrentMonthInstallmentsAsExpenses().sumOf { it.price })

    override fun canPurchase(expenseValue: BigDecimal): Boolean =
        cardLimit
            .minus(getRemainingSpendableValue())
            .minus(expenseValue) >= BigDecimal.ZERO

    override fun getMonthlyExpenses(): List<Expense> =
        getExpensesWithinDateRange(previousInvoiceClosingDate, currentInvoiceClosingDate)

    override fun getMonthlyExpensesValue(): BigDecimal = getMonthlyExpenses().sumOf { it.price }

    override fun getExpensesWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<CreditExpense> {
        val creditExpenses = mutableListOf<CreditExpense>()
        creditExpenses.addAll(expenses?.filterWithinDateRange(startDate, endDate) ?: listOf())
        creditExpenses.addAll(getCurrentMonthInstallmentsAsExpenses())

        return creditExpenses
    }

    override fun getExpensesValueWithinDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal =
        getExpensesWithinDateRange(startDate, endDate).sumOf { it.price }

    override fun processExpense(expense: Expense) {
        TODO("Not yet implemented")
    }

    private fun getOngoingInstallmentsValue(): BigDecimal =
        expenses?.getOngoingInstallmentsValue(previousInvoiceClosingDate) ?: BigDecimal.ZERO

    private fun getCurrentMonthInstallmentsAsExpenses(): List<CreditExpense> =
        expenses?.getOngoingInstallmentsAsExpenses() ?: emptyList()

    private fun getPreviousClosingDate(invoiceClosingDayOfMonth: Int): LocalDate {
        val today = LocalDate.now()

        if (invoiceClosingDayOfMonth > today.dayOfMonth) {
            return getClosingDateWithValidDay(today.minusMonths(1), invoiceClosingDayOfMonth)
        }

        return getClosingDateWithValidDay(today, invoiceClosingDayOfMonth)
    }

    private fun getCurrentClosingDate(invoiceClosingDayOfMonth: Int): LocalDate {
        val today = LocalDate.now()

        if (invoiceClosingDayOfMonth > today.dayOfMonth) {
            return getClosingDateWithValidDay(today, invoiceClosingDayOfMonth)
        }

        return getClosingDateWithValidDay(today.plusMonths(1), invoiceClosingDayOfMonth)
    }

    companion object {
        fun createBlank() = CreditCard(
            id = 0L,
            last4Digits = "",
            expirationDate = LocalDate.MAX,
            cardLimit = BigDecimal.valueOf(Double.MAX_VALUE),
            financialAccount = FinancialAccount.createBlank(),
            invoiceClosingDayOfMonth = 1
        )
    }
}
