package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsAsExpenses
import br.com.zinid.smartwallet.domain.expense.credit.getOngoingInstallmentsValue
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.utils.DateHelper
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
        assertEquals(1, creditCard.invoiceClosingDayOfMonth)
    }

    @Test
    fun `should get remaining spendable value`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))
        val remainingSpendableValue = creditCard.getRemainingSpendableValue()

        val monthlyExpensesValue = creditCard.getMonthlyExpensesValue()
        val ongoingInstallmentsValue = creditCard.expenses
            ?.getOngoingInstallmentsValue(creditCard.previousInvoiceClosingDate) ?: BigDecimal.ZERO
        val currentMonthInstallmentsValue = creditCard.expenses!!.getOngoingInstallmentsAsExpenses().sumOf { it.price }
        val calculatedValue = creditCard.cardLimit
            .minus(monthlyExpensesValue)
            .minus(ongoingInstallmentsValue)
            .add(currentMonthInstallmentsValue)

        assertEquals(calculatedValue, remainingSpendableValue)
    }

    @Test
    fun `should be able to purchase`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))

        val hugeExpense = CreditExpenseFixtures.getRenovationsCreditExpense(creditCard)

        assertFalse(creditCard.canPurchase(hugeExpense.price))
    }

    @Test
    fun `should not be able to purchase`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))

        val foodDeliveryExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)

        assertTrue(creditCard.canPurchase(foodDeliveryExpense.price))
    }

    @Test
    fun `should get expenses within date range`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))

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
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))

        val expensesValue = creditCard.getExpensesValueWithinDateRange(
            creditCard.previousInvoiceClosingDate,
            creditCard.currentInvoiceClosingDate
        )

        val expectedValue =
            CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard).creditCardInstallments!!.firstInstallmentValue
                .add(CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard).price)
                .add(CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard).price)

        assertEquals(expectedValue, expensesValue)
    }

    @Test
    fun `should not get any expenses if start date is after end date`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val creditCard = tempCard.copy(expenses = CreditExpenseFixtures.getCreditExpenseList(tempCard))

        assertThrows<IllegalStateException> {
            creditCard.getExpensesWithinDateRange(
                creditCard.currentInvoiceClosingDate,
                creditCard.previousInvoiceClosingDate
            )
        }
    }

    @Test
    fun `should get previous closing date`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val today = LocalDate.now()

        val previousClosingDate = if (tempCard.invoiceClosingDayOfMonth > today.dayOfMonth) {
            DateHelper.getClosingDateWithValidDay(today.minusMonths(1), tempCard.invoiceClosingDayOfMonth)
        } else {
            DateHelper.getClosingDateWithValidDay(today, tempCard.invoiceClosingDayOfMonth)
        }

        assertEquals(previousClosingDate, tempCard.previousInvoiceClosingDate)
    }

    @Test
    fun `should get current closing date`() {
        val tempCard = CreditCardFixtures.getCreditCard()
        val today = LocalDate.now()

        val previousClosingDate = if (tempCard.invoiceClosingDayOfMonth > today.dayOfMonth) {
            DateHelper.getClosingDateWithValidDay(today, tempCard.invoiceClosingDayOfMonth)
        } else {
            DateHelper.getClosingDateWithValidDay(today.plusMonths(1), tempCard.invoiceClosingDayOfMonth)
        }

        assertEquals(previousClosingDate, tempCard.currentInvoiceClosingDate)
    }
}
