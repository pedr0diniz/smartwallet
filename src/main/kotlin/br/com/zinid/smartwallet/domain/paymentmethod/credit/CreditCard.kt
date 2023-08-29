package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.exception.ExpiredCardException
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.filterWithinDateRange
import br.com.zinid.smartwallet.domain.expense.credit.getInstallmentsWithinDateRangeAsExpenses
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsValue
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.utils.DateHelper.getDateWithValidDay
import br.com.zinid.smartwallet.domain.utils.DateHelper.isAfterOrEqual
import br.com.zinid.smartwallet.domain.utils.DateHelper.isBeforeOrEqual
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class CreditCard(
    override val id: Long = 0L,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    override val financialAccount: FinancialAccount,
    override val expenses: List<CreditExpense> = listOf(),
    val invoiceDueDayOfMonth: Int,
    val today: LocalDate = LocalDate.now()
) : PaymentMethod {

    override val type: PaymentType = PaymentType.CREDIT
    val delayBetweenClosingAndDueDays: Int = DEFAULT_DELAY_BETWEEN_CLOSING_AND_DUE_DAYS.toInt()

    val previousInvoiceDueDate: LocalDate = getPreviousDueDate(today)
    val currentInvoiceDueDate: LocalDate = getCurrentDueDate(today)

    val previousInvoiceClosingDate: LocalDate = getPreviousClosingDate(today)
    val currentInvoiceClosingDate: LocalDate = getCurrentClosingDate(today)

    override fun getRemainingSpendableValue(): BigDecimal = cardLimit
        .minus(getMonthlyExpensesValue(YearMonth.now()))
        .minus(getOngoingInstallmentsValue())
        .add(
            getInstallmentsWithinDateRangeAsExpenses(
                previousInvoiceClosingDate,
                currentInvoiceClosingDate
            ).sumOf { it.price }
        )

    override fun canPurchase(expense: Expense): Boolean =
        getRemainingSpendableValue().minus(expense.price) >= BigDecimal.ZERO && hasNotExpiredByDateOf(expense)

    override fun getMonthlyExpenses(yearMonth: YearMonth?): List<Expense> {
        val (previousClosingDate, currentClosingDate) = if (yearMonth == null || yearMonth == YearMonth.now()) {
            Pair(previousInvoiceClosingDate, currentInvoiceClosingDate)
        } else {
            Pair(
                getPreviousClosingDate(LocalDate.of(yearMonth.year, yearMonth.month, today.dayOfMonth)),
                getCurrentClosingDate(LocalDate.of(yearMonth.year, yearMonth.month, today.dayOfMonth))
            )
        }

        return getExpensesWithinDateRange(previousClosingDate, currentClosingDate)
    }

    override fun getMonthlyExpensesValue(yearMonth: YearMonth?): BigDecimal =
        getMonthlyExpenses(yearMonth).sumOf { it.price }

    override fun getExpensesWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<CreditExpense> {
        val creditExpenses = mutableListOf<CreditExpense>()
        creditExpenses.addAll(expenses.filterWithinDateRange(startDate, endDate))
        creditExpenses.addAll(getInstallmentsWithinDateRangeAsExpenses(startDate, endDate))

        return creditExpenses
    }

    override fun getExpensesValueWithinDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal =
        getExpensesWithinDateRange(startDate, endDate).sumOf { it.price }

    private fun hasNotExpiredByDateOf(expense: Expense): Boolean =
        expirationDate.isAfterOrEqual(expense.date).let {
            if (it) return true
            else throw ExpiredCardException(EXPIRED_CARD_MESSAGE.format(last4Digits, expirationDate, expense.date))
        }

    private fun getOngoingInstallmentsValue(): BigDecimal =
        expenses.getOngoingInstallmentsValue(previousInvoiceClosingDate)

    private fun getInstallmentsWithinDateRangeAsExpenses(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<CreditExpense> = expenses.getInstallmentsWithinDateRangeAsExpenses(startDate, endDate)

    private fun getPreviousDueDate(givenDate: LocalDate?): LocalDate {
        val referenceDate = givenDate ?: today

        return when (invoiceDueDayOfMonth > referenceDate.dayOfMonth) {
            true -> getDateWithValidDay(referenceDate.minusMonths(1), invoiceDueDayOfMonth)
            false -> getDateWithValidDay(referenceDate, invoiceDueDayOfMonth)
        }
    }

    private fun getCurrentDueDate(givenDate: LocalDate?): LocalDate {
        val referenceDate = givenDate ?: today

        return when (invoiceDueDayOfMonth > referenceDate.dayOfMonth) {
            true -> getDateWithValidDay(referenceDate, invoiceDueDayOfMonth)
            false -> getDateWithValidDay(referenceDate.plusMonths(1), invoiceDueDayOfMonth)
        }
    }

    private fun getPreviousClosingDate(givenDate: LocalDate?): LocalDate {
        val referenceDate = givenDate ?: today

        val (currentDueDate, previousDueDate) = if (referenceDate == today) {
            Pair(currentInvoiceDueDate, previousInvoiceDueDate)
        } else {
            Pair(getCurrentDueDate(referenceDate), getPreviousDueDate(referenceDate))
        }

        val invoiceDueDate = when (givenDayIsBetweenClosingDayAndDueDay(referenceDate)) {
            true -> currentDueDate
            false -> previousDueDate
        }
        val invoiceClosingDay = invoiceDueDate.minusDays(delayBetweenClosingAndDueDays.toLong()).dayOfMonth

        return when (invoiceClosingDay > invoiceDueDate.dayOfMonth) {
            true -> getDateWithValidDay(invoiceDueDate.minusMonths(1), invoiceClosingDay)
            false -> getDateWithValidDay(invoiceDueDate, invoiceClosingDay)
        }
    }

    private fun getCurrentClosingDate(givenDate: LocalDate?): LocalDate {
        val referenceDate = givenDate ?: today

        val currentDueDate = if (referenceDate == today) currentInvoiceDueDate
        else getCurrentDueDate(referenceDate)

        val invoiceDueDate = when (givenDayIsBetweenClosingDayAndDueDay(referenceDate)) {
            true -> currentDueDate.plusMonths(1)
            false -> currentDueDate
        }
        val invoiceClosingDay = invoiceDueDate.minusDays(delayBetweenClosingAndDueDays.toLong()).dayOfMonth

        return when (invoiceClosingDay > invoiceDueDate.dayOfMonth) {
            true -> getDateWithValidDay(invoiceDueDate.minusMonths(1), invoiceClosingDay)
            false -> getDateWithValidDay(invoiceDueDate, invoiceClosingDay)
        }
    }

    private fun givenDayIsBetweenClosingDayAndDueDay(givenDate: LocalDate): Boolean =
        currentInvoiceDueDate.minusDays(delayBetweenClosingAndDueDays.toLong()).isBeforeOrEqual(givenDate)

    companion object {
        private const val DEFAULT_DELAY_BETWEEN_CLOSING_AND_DUE_DAYS = 10L
        fun createBlank() = CreditCard(
            id = 0L,
            last4Digits = "",
            expirationDate = LocalDate.MAX,
            cardLimit = BigDecimal.valueOf(Double.MAX_VALUE),
            financialAccount = FinancialAccount.createBlank(),
            invoiceDueDayOfMonth = 11,
            today = LocalDate.now()
        )

        fun createBlankFromId(id: Long) = CreditCard(
            id = id,
            last4Digits = "",
            expirationDate = LocalDate.MAX,
            cardLimit = BigDecimal.valueOf(Double.MAX_VALUE),
            financialAccount = FinancialAccount.createBlank(),
            invoiceDueDayOfMonth = 11,
            today = LocalDate.now()
        )

        private const val EXPIRED_CARD_MESSAGE =
            "Card ending with [%s] expires by [%s] and is expired by expense date [%s]"
    }
}
