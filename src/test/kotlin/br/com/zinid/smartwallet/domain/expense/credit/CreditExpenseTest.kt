package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CreditExpenseTest {

    private val today = LocalDate.now()
    private val nextMonth = today.plusMonths(1)

    @Test
    fun `should create blank`() {
        val creditExpense = CreditExpense.createBlank()

        assertEquals(0L, creditExpense.id)
        assertEquals("", creditExpense.content)
        assertEquals(LocalDate.now(), creditExpense.date)
        assertEquals(BigDecimal.ZERO, creditExpense.price)
        assertFalse(creditExpense.essential!!)
        assertFalse(creditExpense.monthlySubscription!!)
        assertEquals(CreditCard.createBlank(), creditExpense.paymentMethod)
    }
    @Test
    fun `should get payment type`() {
        val creditExpense = CreditExpense.createBlank()
        assertEquals(PaymentType.CREDIT, creditExpense.getPaymentType())
    }

    @Test
    fun `should have installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        assertTrue(creditExpense.hasInstallments())
    }

    @Test
    fun `should not have installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)

        assertFalse(creditExpense.hasInstallments())
    }

    @Test
    fun `should build installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        assertNull(creditExpense.creditCardInstallments)

        creditExpense.process()

        assertNotNull(creditExpense.creditCardInstallments)
    }

    @Test
    fun `should get installments by period`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        val installments = creditExpense.getCreditCardInstallmentsByPeriod(today, nextMonth)

        assertTrue(installments.isNotEmpty())
    }

    @Test
    fun `should not find any installments for given period`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)

        val installments = creditExpense.getCreditCardInstallmentsByPeriod(today, nextMonth)

        assertTrue(installments.isEmpty())
    }

    @Test
    fun `should get current month installment as expense`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        val currentInstallmentAsExpense = creditExpense.getCurrentMonthInstallmentAsExpense()
        val expectedInstallment = CreditCardInstallments.createFromExpenseAndCreditCard(creditExpense, creditCard)

        val expectedExpense = CreditExpense(
            content = "Parcela de ${creditExpense.content}",
            date = creditExpense.date,
            price = expectedInstallment.firstInstallmentValue, // TODO - fix this after credit card due day update
            paymentMethod = creditExpense.paymentMethod,
            essential = creditExpense.essential,
            monthlySubscription = creditExpense.monthlySubscription,
            expenseFor = creditExpense.expenseFor
        )

        assertNotNull(currentInstallmentAsExpense)
        assertEquals(expectedExpense, currentInstallmentAsExpense)
    }

    @Test
    fun `should not find a current month installment to convert to expense`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)

        assertNull(creditExpense.getCurrentMonthInstallmentAsExpense())
    }

    @Test
    fun `should process`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        assertTrue(creditExpense.process())
    }

    @Test
    fun `should not process`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getRenovationsCreditExpense(creditCard)

        assertFalse(creditExpense.process())
    }

    @Test
    fun `should find expenses when filtering within date range`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        assertTrue(
            creditExpenses.filterWithinDateRange(
                creditCard.previousInvoiceClosingDate,
                creditCard.currentInvoiceClosingDate
            ).isNotEmpty()
        )
    }

    @Test
    fun `should not find expenses when filtering within date range`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        assertTrue(
            creditExpenses.filterWithinDateRange(
                today.minusMonths(3),
                today.minusMonths(2)
            ).isEmpty()
        )
    }

    @Test
    fun `should get ongoing installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        assertTrue(creditExpenses.getOngoingInstallments(creditCard.previousInvoiceClosingDate).isNotEmpty())
    }

    @Test
    fun `should not find any ongoing installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = listOf(
            CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard),
            CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)
        )

        assertTrue(creditExpenses.getOngoingInstallments(creditCard.previousInvoiceClosingDate).isEmpty())
    }

    @Test
    fun `should get ongoing installments value`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        val expectedValue = creditExpenses
            .filter { it.creditCardInstallments != null }
            .sumOf { it.price }

        assertEquals(expectedValue, creditExpenses.getOngoingInstallmentsValue(creditCard.previousInvoiceClosingDate))
    }

    @Test
    fun `should get current month installments as expenses`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = listOf(CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard))

        val expectedExpense = creditExpenses.first().getCurrentMonthInstallmentAsExpense()

        val installmentsAsExpenses = creditExpenses.getCurrentMonthInstallmentsAsExpenses()

        assertEquals(expectedExpense, installmentsAsExpenses.first())
    }

    @Test
    fun `should not find any ongoing installments to return as expense`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpenses = listOf(
            CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard),
            CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)
        )

        assertTrue(creditExpenses.getCurrentMonthInstallmentsAsExpenses().isEmpty())
    }
}
