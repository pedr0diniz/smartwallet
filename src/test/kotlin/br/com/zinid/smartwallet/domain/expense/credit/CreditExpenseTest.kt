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
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        val installments = creditExpense.getCreditCardInstallmentsByPeriod(today, nextMonth)

        assertTrue(installments.isNotEmpty())
    }

    @Test
    fun `should not find any installments for given period`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpense = CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)

        val installments = creditExpense.getCreditCardInstallmentsByPeriod(today, nextMonth)

        assertTrue(installments.isEmpty())
    }

    @Test
    fun `should get current month installment as expense with early month closing date`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        val currentInstallmentAsExpense = creditExpense.getInstallmentWithinDateRangeAsExpense()
        val expectedInstallment = CreditCardInstallments.createFromExpenseAndCreditCard(creditExpense, creditCard)

        val expectedExpense = CreditExpense(
            content = "Parcela 1 / 10 de ${creditExpense.content}",
            date = creditExpense.date,
            price = expectedInstallment.firstInstallmentValue,
            paymentMethod = creditExpense.paymentMethod,
            essential = creditExpense.essential,
            monthlySubscription = creditExpense.monthlySubscription
        )

        assertNotNull(currentInstallmentAsExpense)
        assertEquals(expectedExpense, currentInstallmentAsExpense)
    }

    @Test
    fun `should get current month installment as expense with late month closing date`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            today = today.withDayOfMonth(1),
            invoiceDueDayOfMonth = 5
        )
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        val currentInstallmentAsExpense = creditExpense.getInstallmentWithinDateRangeAsExpense()
        val expectedInstallment = CreditCardInstallments.createFromExpenseAndCreditCard(creditExpense, creditCard)

        val expectedExpense = CreditExpense(
            content = "Parcela 1 / 10 de ${creditExpense.content}",
            date = creditExpense.date,
            price = expectedInstallment.firstInstallmentValue,
            paymentMethod = creditExpense.paymentMethod,
            essential = creditExpense.essential,
            monthlySubscription = creditExpense.monthlySubscription
        )

        assertNotNull(currentInstallmentAsExpense)
        assertEquals(expectedExpense, currentInstallmentAsExpense)
    }

    @Test
    fun `should not find a current month installment to convert to expense`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)

        assertNull(creditExpense.getInstallmentWithinDateRangeAsExpense())
    }

    @Test
    fun `should process`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        creditExpense.process()

        assertNotNull(creditExpense.numberOfInstallments)
        assertNotNull(creditExpense.creditCardInstallments)
    }

    @Test
    fun `should not process`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpense = CreditExpenseFixtures.getRenovationsCreditExpense(creditCard)

        creditExpense.process()
    }

    @Test
    fun `should find expenses when filtering within date range`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
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
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        assertTrue(
            creditExpenses.filterWithinDateRange(
                today.minusMonths(3),
                today.minusMonths(2)
            ).isEmpty()
        )
    }

    @Test
    fun `should filter by tag and return results`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard).map { it.copy(tag = "TESTE") }

        assertTrue(creditExpenses.filterByTag("TESTE").isNotEmpty())
    }

    @Test
    fun `should filter by tag and return no results`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        assertTrue(creditExpenses.filterByTag("TESTE").isEmpty())
    }

    @Test
    fun `should get ongoing installments`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        assertTrue(creditExpenses.getOngoingInstallments(creditCard.previousInvoiceClosingDate).isNotEmpty())
    }

    @Test
    fun `should not find any ongoing installments`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = listOf(
            CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard),
            CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)
        )

        assertTrue(creditExpenses.getOngoingInstallments(creditCard.previousInvoiceClosingDate).isEmpty())
    }

    @Test
    fun `should get ongoing installments value`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        val expectedValue = creditExpenses
            .filter { it.creditCardInstallments != null }
            .sumOf { it.price }

        assertEquals(expectedValue, creditExpenses.getOngoingInstallmentsValue(creditCard.previousInvoiceClosingDate))
    }

    @Test
    fun `should get current month installments as expenses`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = listOf(CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard))

        val expectedExpense = creditExpenses.first().getInstallmentWithinDateRangeAsExpense()

        val installmentsAsExpenses = creditExpenses.getInstallmentsWithinDateRangeAsExpenses(
            creditCard.previousInvoiceClosingDate,
            creditCard.currentInvoiceClosingDate
        )

        assertEquals(expectedExpense, installmentsAsExpenses.first())
    }

    @Test
    fun `should not find any ongoing installments to return as expense`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(today = today.withDayOfMonth(12))
        val creditExpenses = listOf(
            CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard),
            CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)
        )

        assertTrue(
            creditExpenses.getInstallmentsWithinDateRangeAsExpenses(
                creditCard.previousInvoiceClosingDate,
                creditCard.currentInvoiceClosingDate
            ).isEmpty()
        )
    }
}
