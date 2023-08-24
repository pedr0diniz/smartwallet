package br.com.zinid.smartwallet.domain.creditcardinstallments

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.exception.NoInstallmentsException
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CreditCardInstallmentsTest {
    @Test
    fun `should get ongoing installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val ongoingInstallments = creditCardInstallments?.getOngoingInstallments(creditExpense.date)

        assertEquals(creditCardInstallments?.installments, ongoingInstallments)
    }

    @Test
    fun `should not find ongoing installments to get`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val ongoingInstallments = creditCardInstallments!!.getOngoingInstallments(LocalDate.MAX)

        assertTrue(ongoingInstallments.isEmpty())
    }

    @Test
    fun `should get ongoing installments value`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val ongoingInstallmentsValue = creditCardInstallments?.getOngoingInstallmentsValue(creditExpense.date)

        assertEquals(creditExpense.price, ongoingInstallmentsValue)
    }

    @Test
    fun `should get installments by period`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val installmentsByPeriod = creditCardInstallments?.getInstallmentsByPeriod(creditExpense.date, LocalDate.MAX)

        assertEquals(creditCardInstallments?.installments, installmentsByPeriod)
    }

    @Test
    fun `should not find any installments by period`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val installmentsByPeriod = creditCardInstallments!!.getInstallmentsByPeriod(LocalDate.MAX, LocalDate.MAX)

        assertTrue(installmentsByPeriod.isEmpty())
    }

    @Test
    fun `should build installments list when the expense date is before the dueDay`() {
        val creditCard = CreditCardFixtures.getCreditCard()
            .apply { this.copy(invoiceDueDayOfMonth = 28) }
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        creditExpense.process()

        val installmentsList = creditExpense.creditCardInstallments!!.buildInstallmentsList()

        assertEquals(creditExpense.numberOfInstallments, installmentsList.size)
        assertEquals(creditExpense.price, installmentsList.sumOf { it.installmentValue })
    }

    @Test
    fun `should build installments list when the expense date is after the dueDay`() {
        val creditCard = CreditCardFixtures.getCreditCard().copy(invoiceDueDayOfMonth = 1)
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        creditExpense.process()

        val installmentsList = creditExpense.creditCardInstallments!!.buildInstallmentsList()

        assertEquals(creditExpense.numberOfInstallments, installmentsList.size)
        assertEquals(creditExpense.price, installmentsList.sumOf { it.installmentValue })
    }

    @Test
    fun `should throw exception when creating from expense without installments`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)

        assertThrows<NoInstallmentsException> {
            CreditCardInstallments.createFromExpenseAndCreditCard(creditExpense, creditCard)
        }
    }

    @Test
    fun `should create from expense and credit card`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        assertDoesNotThrow { CreditCardInstallments.createFromExpenseAndCreditCard(creditExpense, creditCard) }
    }
}
