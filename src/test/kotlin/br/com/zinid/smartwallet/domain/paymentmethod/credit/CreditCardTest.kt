package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.exception.ExpiredCardException
import br.com.zinid.smartwallet.domain.exception.InvalidDateRangeException
import br.com.zinid.smartwallet.domain.expense.credit.getCurrentMonthInstallmentsAsExpenses
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsValue
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.utils.DateHelper.getDateWithValidDay
import br.com.zinid.smartwallet.domain.utils.DateHelper.isBeforeOrEqual
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CreditCardTest {

    @Test
    fun `should create blank credit card`() {
        val creditCard = CreditCard.createBlank()

        assertEquals(0L, creditCard.id)
        assertEquals("", creditCard.last4Digits)
        assertEquals(LocalDate.MAX, creditCard.expirationDate)
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), creditCard.cardLimit)
        assertEquals(FinancialAccount.createBlank(), creditCard.financialAccount)
        assertEquals(11, creditCard.invoiceDueDayOfMonth)
        assertEquals(LocalDate.now(), creditCard.today)
    }

    @Test
    fun `should create blank credit card from id`() {
        val id = 100L
        val creditCard = CreditCard.createBlankFromId(id)

        assertEquals(id, creditCard.id)
        assertEquals("", creditCard.last4Digits)
        assertEquals(LocalDate.MAX, creditCard.expirationDate)
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), creditCard.cardLimit)
        assertEquals(FinancialAccount.createBlank(), creditCard.financialAccount)
        assertEquals(11, creditCard.invoiceDueDayOfMonth)
        assertEquals(LocalDate.now(), creditCard.today)
    }

    @Test
    fun `should get remaining spendable value`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(
            expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard),
            today = LocalDate.now().withDayOfMonth(13)
        )
        val remainingSpendableValue = creditCard.getRemainingSpendableValue()

        val monthlyExpensesValue = creditCard.getMonthlyExpensesValue()

        val ongoingInstallmentsValue = creditCard.expenses
            ?.getOngoingInstallmentsValue(creditCard.previousInvoiceClosingDate) ?: BigDecimal.ZERO

        val currentMonthInstallmentsValue =
            creditCard.expenses!!.getCurrentMonthInstallmentsAsExpenses().sumOf { it.price }

        val calculatedValue = creditCard.cardLimit
            .minus(monthlyExpensesValue)
            .minus(ongoingInstallmentsValue)
            .add(currentMonthInstallmentsValue)

        assertEquals(calculatedValue, remainingSpendableValue)
    }

    @Test
    fun `should be able to purchase`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(
            expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard),
            today = LocalDate.now().withDayOfMonth(10)
        )

        val foodDeliveryExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)

        assertTrue(creditCard.canPurchase(foodDeliveryExpense))
    }

    @Test
    fun `should not be able to purchase when the card has no limit`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(
            expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard),
            today = LocalDate.now().withDayOfMonth(10)
        )

        val hugeExpense = CreditExpenseFixtures.getRenovationsCreditExpense(creditCard)

        assertFalse(creditCard.canPurchase(hugeExpense))
    }

    @Test
    fun `should not be able to purchase when the card is expired`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(
            expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard),
            today = LocalDate.now().withDayOfMonth(10),
            expirationDate = LocalDate.now().minusYears(1)
        )

        val foodDeliveryExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)

        assertThrows<ExpiredCardException> { creditCard.canPurchase(foodDeliveryExpense) }
    }

    @Test
    fun `should get expenses within date range`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(
            expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard),
            today = LocalDate.now().withDayOfMonth(13)
        )

        val expenses = creditCard.getExpensesWithinDateRange(
            creditCard.previousInvoiceClosingDate,
            creditCard.currentInvoiceClosingDate
        )

        assertEquals(3, expenses.size)
        assertEquals(expenses.sumOf { it.price }, creditCard.getMonthlyExpensesValue())
    }

    @Test
    fun `should not find any expense within date range`() {
        val creditCard = CreditCardFixtures.getCreditCard()

        val expenses = creditCard.getExpensesWithinDateRange(
            creditCard.previousInvoiceClosingDate,
            creditCard.currentInvoiceClosingDate
        )

        assertTrue(expenses.isEmpty())
    }

    @Test
    fun `should get expenses value within date range`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(
            expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard),
            today = LocalDate.now().withDayOfMonth(13)
        )

        val expensesValue = creditCard.getExpensesValueWithinDateRange(
            creditCard.previousInvoiceClosingDate,
            creditCard.currentInvoiceClosingDate
        )

        val expectedValue =
            CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard).creditCardInstallments!!.installmentValue
                .add(CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard).price)
                .add(CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard).price)

        assertEquals(expectedValue, expensesValue)
    }

    @Test
    fun `should not get any expenses if start date is after end date`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))

        assertThrows<InvalidDateRangeException> {
            creditCard.getExpensesWithinDateRange(
                creditCard.currentInvoiceClosingDate,
                creditCard.previousInvoiceClosingDate
            )
        }
    }

    @Test
    fun `should get previous due date`() {
        val today = LocalDate.now().withDayOfMonth(3)
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today)

        val previousInvoiceDueDate = getDateWithValidDay(
            today.minusMonths(1),
            creditCard.invoiceDueDayOfMonth
        )

        assertEquals(previousInvoiceDueDate, creditCard.previousInvoiceDueDate)
    }

    @Test
    fun `should get previous due date when the due day is 31`() {
        val today = LocalDate.now().withDayOfMonth(3)
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            invoiceDueDayOfMonth = 31,
            today = today
        )

        val previousInvoiceDueDate = getDateWithValidDay(
            today.minusMonths(1),
            creditCard.invoiceDueDayOfMonth
        )

        assertEquals(previousInvoiceDueDate, creditCard.previousInvoiceDueDate)
    }

    @Test
    fun `should get current due date`() {
        val today = LocalDate.now().withDayOfMonth(3)
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today)

        val currentInvoiceDueDate = getDateWithValidDay(
            today,
            creditCard.invoiceDueDayOfMonth
        )

        assertEquals(currentInvoiceDueDate, creditCard.currentInvoiceDueDate)
    }

    @Test
    fun `should get current due date when the due day is 31`() {
        val today = LocalDate.now().withDayOfMonth(3)
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today)

        val currentInvoiceDueDate = getDateWithValidDay(
            today,
            creditCard.invoiceDueDayOfMonth
        )

        assertEquals(currentInvoiceDueDate, creditCard.currentInvoiceDueDate)
    }

    @Test
    fun `should get closing dates when outside closing-due days and due date is after today`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            invoiceDueDayOfMonth = 15,
            today = LocalDate.parse("2023-07-03")
        )

        val dueDate = getInvoiceDueDateForCurrentClosingDate(creditCard)
        val invoiceClosingDay = dueDate.minusDays(10).dayOfMonth

        assertFalse(todayIsBetweenClosingDayAndDueDay(creditCard))
        assertFalse(invoiceClosingDay > dueDate.dayOfMonth)

        assertEquals(LocalDate.parse("2023-06-05"), creditCard.previousInvoiceClosingDate)
        assertEquals(LocalDate.parse("2023-07-05"), creditCard.currentInvoiceClosingDate)
    }

    @Test
    fun `should get closing dates when outside closing-due days and due date is before today`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            invoiceDueDayOfMonth = 1,
            today = LocalDate.parse("2023-07-15")
        )

        val dueDate = getInvoiceDueDateForCurrentClosingDate(creditCard)
        val invoiceClosingDay = dueDate.minusDays(10).dayOfMonth

        assertFalse(todayIsBetweenClosingDayAndDueDay(creditCard))
        assertTrue(invoiceClosingDay > dueDate.dayOfMonth)

        assertEquals(LocalDate.parse("2023-06-21"), creditCard.previousInvoiceClosingDate)
        assertEquals(LocalDate.parse("2023-07-22"), creditCard.currentInvoiceClosingDate)
    }

    @Test
    fun `should get closing dates when inside closing-due days and due date is after today`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            invoiceDueDayOfMonth = 11,
            today = LocalDate.parse("2023-07-05")
        )

        val dueDate = getInvoiceDueDateForCurrentClosingDate(creditCard)
        val invoiceClosingDay = dueDate.minusDays(10).dayOfMonth

        assertTrue(todayIsBetweenClosingDayAndDueDay(creditCard))
        assertFalse(invoiceClosingDay > dueDate.dayOfMonth)

        assertEquals(LocalDate.parse("2023-07-01"), creditCard.previousInvoiceClosingDate)
        assertEquals(LocalDate.parse("2023-08-01"), creditCard.currentInvoiceClosingDate)
    }

    @Test
    fun `should get closing dates when inside closing-due days and due date is before today`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            invoiceDueDayOfMonth = 5,
            today = LocalDate.parse("2023-07-27")
        )

        val dueDate = getInvoiceDueDateForCurrentClosingDate(creditCard)
        val invoiceClosingDay = dueDate.minusDays(10).dayOfMonth

        assertTrue(todayIsBetweenClosingDayAndDueDay(creditCard))
        assertTrue(invoiceClosingDay > dueDate.dayOfMonth)

        assertEquals(LocalDate.parse("2023-07-26"), creditCard.previousInvoiceClosingDate)
        assertEquals(LocalDate.parse("2023-08-26"), creditCard.currentInvoiceClosingDate)
    }

    private fun todayIsBetweenClosingDayAndDueDay(creditCard: CreditCard): Boolean =
        creditCard.currentInvoiceDueDate.minusDays(DELAY_BETWEEN_CLOSING_AND_DUE_DAYS).isBeforeOrEqual(creditCard.today)

    private fun getInvoiceDueDateForCurrentClosingDate(creditCard: CreditCard): LocalDate =
        when (creditCard.invoiceDueDayOfMonth > creditCard.today.dayOfMonth) {
            true -> getDateWithValidDay(creditCard.today, creditCard.invoiceDueDayOfMonth)
            false -> getDateWithValidDay(creditCard.today.plusMonths(1), creditCard.invoiceDueDayOfMonth)
        }

    companion object {
        private const val DELAY_BETWEEN_CLOSING_AND_DUE_DAYS = 10L
    }
}
