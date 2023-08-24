package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.exception.ExpiredCardException
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.filterWithinDateRange
import br.com.zinid.smartwallet.domain.expense.credit.getCurrentMonthInstallmentsAsExpenses
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsValue
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.utils.DateHelper.getDateWithValidDay
import br.com.zinid.smartwallet.domain.utils.DateHelper.isAfterOrEqual
import br.com.zinid.smartwallet.domain.utils.DateHelper.isBeforeOrEqual
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCard(
    override val id: Long? = null,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    override val financialAccount: FinancialAccount,
    override val expenses: List<CreditExpense>? = listOf(),
    val invoiceDueDayOfMonth: Int,
    val today: LocalDate = LocalDate.now()
) : PaymentMethod {

    override val type: PaymentType = PaymentType.CREDIT

    val previousInvoiceDueDate: LocalDate = getPreviousDueDate()
    val currentInvoiceDueDate: LocalDate = getCurrentDueDate()

    val previousInvoiceClosingDate: LocalDate = getPreviousClosingDate()
    val currentInvoiceClosingDate: LocalDate = getCurrentClosingDate()

    override fun getRemainingSpendableValue(): BigDecimal = cardLimit
        .minus(getMonthlyExpensesValue())
        .minus(getOngoingInstallmentsValue())
        .add(getCurrentMonthInstallmentsAsExpenses().sumOf { it.price })

    override fun canPurchase(expense: Expense): Boolean =
        getRemainingSpendableValue().minus(expense.price) >= BigDecimal.ZERO && hasNotExpiredByDateOf(expense)

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

    private fun hasNotExpiredByDateOf(expense: Expense): Boolean =
        expirationDate.isAfterOrEqual(expense.date).let {
            if (it) return it
            else throw ExpiredCardException(EXPIRED_CARD_MESSAGE.format(last4Digits, expirationDate, expense.date))
        }

    private fun getOngoingInstallmentsValue(): BigDecimal =
        expenses?.getOngoingInstallmentsValue(previousInvoiceClosingDate) ?: BigDecimal.ZERO

    private fun getCurrentMonthInstallmentsAsExpenses(): List<CreditExpense> =
        expenses?.getCurrentMonthInstallmentsAsExpenses() ?: emptyList()

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

    private fun getPreviousClosingDate(): LocalDate {
        val invoiceDueDate = when (todayIsBetweenClosingDayAndDueDay()) {
            true -> currentInvoiceDueDate
            false -> previousInvoiceDueDate
        }
        val invoiceClosingDay = invoiceDueDate.minusDays(DELAY_BETWEEN_CLOSING_AND_DUE_DAYS).dayOfMonth

        return when (invoiceClosingDay > invoiceDueDate.dayOfMonth) {
            true -> getDateWithValidDay(invoiceDueDate.minusMonths(1), invoiceClosingDay)
            false -> getDateWithValidDay(invoiceDueDate, invoiceClosingDay)
        }
    }

    private fun getCurrentClosingDate(): LocalDate {
        val invoiceDueDate = when (todayIsBetweenClosingDayAndDueDay()) {
            true -> currentInvoiceDueDate.plusMonths(1)
            false -> currentInvoiceDueDate
        }
        val invoiceClosingDay = invoiceDueDate.minusDays(DELAY_BETWEEN_CLOSING_AND_DUE_DAYS).dayOfMonth

        return when (invoiceClosingDay > invoiceDueDate.dayOfMonth) {
            true -> getDateWithValidDay(invoiceDueDate.minusMonths(1), invoiceClosingDay)
            false -> getDateWithValidDay(invoiceDueDate, invoiceClosingDay)
        }
    }

    private fun todayIsBetweenClosingDayAndDueDay(): Boolean =
        currentInvoiceDueDate.minusDays(DELAY_BETWEEN_CLOSING_AND_DUE_DAYS).isBeforeOrEqual(today)

    companion object {
        private const val DELAY_BETWEEN_CLOSING_AND_DUE_DAYS = 10L
        fun createBlank() = CreditCard(
            id = 0L,
            last4Digits = "",
            expirationDate = LocalDate.MAX,
            cardLimit = BigDecimal.valueOf(Double.MAX_VALUE),
            financialAccount = FinancialAccount.createBlank(),
            invoiceDueDayOfMonth = 11
        )

        fun createBlankFromId(id: Long) = CreditCard(
            id = id,
            last4Digits = "",
            expirationDate = LocalDate.MAX,
            cardLimit = BigDecimal.valueOf(Double.MAX_VALUE),
            financialAccount = FinancialAccount.createBlank(),
            invoiceDueDayOfMonth = 11
        )

        private const val EXPIRED_CARD_MESSAGE =
            "Card ending with [%s] expires by [%s] and is expired by expense date [%s]"
    }
}
