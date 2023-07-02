package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.filterWithinDateRange
import br.com.zinid.smartwallet.domain.expense.credit.getCurrentMonthInstallmentsAsExpenses
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsValue
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.utils.DateHelper.getDateWithValidDay
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCard(
    override val id: Long? = null,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    override val financialAccount: FinancialAccount,
    override val expenses: List<CreditExpense>? = listOf(),
    val invoiceClosingDayOfMonth: Int, // TODO - allow users to add the due date of their card rather than the closing day
    val invoiceDueDayOfMonth: Int
) : PaymentMethod {

    override val type: PaymentType = PaymentType.CREDIT
    private val today = LocalDate.now()

    val previousInvoiceDueDate: LocalDate = getPreviousDueDate()
    val currentInvoiceDueDate: LocalDate = getCurrentDueDate()

    val previousInvoiceClosingDate: LocalDate = getPreviousClosingDate(invoiceClosingDayOfMonth)
    val currentInvoiceClosingDate: LocalDate = getCurrentClosingDate(invoiceClosingDayOfMonth)

    override fun getRemainingSpendableValue(): BigDecimal = cardLimit
        .minus(getMonthlyExpensesValue())
        .minus(getOngoingInstallmentsValue())
        .add(getCurrentMonthInstallmentsAsExpenses().sumOf { it.price })

    override fun canPurchase(expenseValue: BigDecimal): Boolean =
        getRemainingSpendableValue()
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

    override fun processExpense(expense: Expense): Boolean = canPurchase(expense.price)

    private fun getOngoingInstallmentsValue(): BigDecimal =
        expenses?.getOngoingInstallmentsValue(previousInvoiceClosingDate) ?: BigDecimal.ZERO

    private fun getCurrentMonthInstallmentsAsExpenses(): List<CreditExpense> =
        expenses?.getCurrentMonthInstallmentsAsExpenses() ?: emptyList()

    private fun getPreviousClosingDate(invoiceClosingDayOfMonth: Int): LocalDate {
        return when (invoiceClosingDayOfMonth > today.dayOfMonth) {
            true -> getDateWithValidDay(today.minusMonths(1), invoiceClosingDayOfMonth)
            false -> getDateWithValidDay(today, invoiceClosingDayOfMonth)
        }
    }

    private fun getCurrentClosingDate(invoiceClosingDayOfMonth: Int): LocalDate {
        return when (invoiceClosingDayOfMonth > today.dayOfMonth) {
            true -> getDateWithValidDay(today, invoiceClosingDayOfMonth)
            false -> getDateWithValidDay(today.plusMonths(1), invoiceClosingDayOfMonth)
        }
    }

    private fun getPreviousDueDate(): LocalDate {
        return when (invoiceDueDayOfMonth > today.dayOfMonth) {
            true -> getDateWithValidDay(today.minusMonths(1), invoiceDueDayOfMonth)
            false -> getDateWithValidDay(today, invoiceDueDayOfMonth)
        }
    }
    private fun getCurrentDueDate(): LocalDate {
        return when (invoiceDueDayOfMonth > today.dayOfMonth) {
            true -> getDateWithValidDay(today, invoiceDueDayOfMonth)
            false -> getDateWithValidDay(today.plusMonths(1), invoiceDueDayOfMonth)
        }
    }

    companion object {
        private const val DELAY_BETWEEN_CLOSING_AND_DUE_DAYS = 10
        fun createBlank() = CreditCard(
            id = 0L,
            last4Digits = "",
            expirationDate = LocalDate.MAX,
            cardLimit = BigDecimal.valueOf(Double.MAX_VALUE),
            financialAccount = FinancialAccount.createBlank(),
            invoiceClosingDayOfMonth = 1,
            invoiceDueDayOfMonth = 10
        )
    }
}
