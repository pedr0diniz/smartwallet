package br.com.zinid.smartwallet.application.adapter.expense

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseEntity
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class FindExpenseAdapterTest {
    private val expenseRepository = mockk<ExpenseRepository>()
    private val findExpenseAdapter = FindExpenseAdapter(expenseRepository)

    @Test
    fun `should find by credit expense by id`() {
        val expenseId = 2L
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditExpenseEntity = CreditExpenseEntity.fromDomain(creditExpense)
        val expenseEntity = ExpenseEntity(id = 1L, creditExpense = creditExpenseEntity)

        every { expenseRepository.findByIdOrNull(expenseId) } returns expenseEntity

        val possibleCreditExpense = findExpenseAdapter.findById(expenseId)

        verify(exactly = 1) { expenseRepository.findByIdOrNull(expenseId) }

        assertEquals(creditExpense, possibleCreditExpense)
    }

    @Test
    fun `should find debit expense by id`() {
        val expenseId = 2L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpense = DebitExpenseFixtures.getElectricalPowerBillDebitExpense(debitPaymentMethod)
        val debitExpenseEntity = DebitExpenseEntity.fromDomain(debitExpense)
        val expenseEntity = ExpenseEntity(id = 1L, debitExpense = debitExpenseEntity)

        every { expenseRepository.findByIdOrNull(expenseId) } returns expenseEntity

        val possibleDebitExpense = findExpenseAdapter.findById(expenseId)

        verify(exactly = 1) { expenseRepository.findByIdOrNull(expenseId) }

        assertEquals(debitExpense, possibleDebitExpense)
    }

    @Test
    fun `should not find an expense by id`() {
        val expenseId = 2L

        every { expenseRepository.findByIdOrNull(expenseId) } returns null

        assertNull(findExpenseAdapter.findById(expenseId))

        verify(exactly = 1) { expenseRepository.findByIdOrNull(expenseId) }
    }

    @Test
    fun `should find a credit expense by credit expense id`() {
        val creditExpenseId = 2L
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
            .copy(id = creditExpenseId)
        val creditExpenseEntity = CreditExpenseEntity.fromDomain(creditExpense)
        val expenseEntity = ExpenseEntity(id = 1L, creditExpense = creditExpenseEntity)

        every { expenseRepository.findByCreditExpenseId(creditExpenseId) } returns expenseEntity

        val possibleCreditExpense = findExpenseAdapter.findByCreditExpenseId(creditExpenseId)

        verify(exactly = 1) { expenseRepository.findByCreditExpenseId(creditExpenseId) }

        assertEquals(creditExpense, possibleCreditExpense)
    }

    @Test
    fun `should not find a credit expense by credit expense id`() {
        val creditExpenseId = 2L

        every { expenseRepository.findByCreditExpenseId(creditExpenseId) } returns null

        assertNull(findExpenseAdapter.findByCreditExpenseId(creditExpenseId))

        verify(exactly = 1) { expenseRepository.findByCreditExpenseId(creditExpenseId) }
    }

    @Test
    fun `should find a debit expense by debit expense id`() {
        val debitExpenseId = 2L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpense = DebitExpenseFixtures.getElectricalPowerBillDebitExpense(debitPaymentMethod)
            .copy(id = debitExpenseId)
        val debitExpenseEntity = DebitExpenseEntity.fromDomain(debitExpense)
        val expenseEntity = ExpenseEntity(id = 1L, debitExpense = debitExpenseEntity)

        every { expenseRepository.findByDebitExpenseId(debitExpenseId) } returns expenseEntity

        val possibleDebitExpense = findExpenseAdapter.findByDebitExpenseId(debitExpenseId)

        verify(exactly = 1) { expenseRepository.findByDebitExpenseId(debitExpenseId) }

        assertEquals(debitExpense, possibleDebitExpense)
    }

    @Test
    fun `should not find a debit expense by debit expense id`() {
        val debitExpenseId = 2L

        every { expenseRepository.findByDebitExpenseId(debitExpenseId) } returns null

        assertNull(findExpenseAdapter.findByDebitExpenseId(debitExpenseId))

        verify(exactly = 1) { expenseRepository.findByDebitExpenseId(debitExpenseId) }
    }
}
