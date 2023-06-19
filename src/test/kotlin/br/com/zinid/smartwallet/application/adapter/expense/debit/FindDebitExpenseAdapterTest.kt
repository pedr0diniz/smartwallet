package br.com.zinid.smartwallet.application.adapter.expense.debit

import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseRepository
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.FindDebitExpenseAdapter
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class FindDebitExpenseAdapterTest {

    private val debitExpenseRepository = mockk<DebitExpenseRepository>()
    private val findDebitExpenseAdapter = FindDebitExpenseAdapter(debitExpenseRepository)

    @Test
    fun `should find debit expense by id`() {
        val expenseId = 7L
        val debitPaymentMethodId = 10L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)
        val debitExpense = DebitExpenseFixtures.getWaterBillDebitExpense(debitPaymentMethod)
        val debitExpenseEntity = DebitExpenseEntity.fromDomain(debitExpense)

        every { debitExpenseRepository.findByIdOrNull(expenseId) } returns debitExpenseEntity

        val possibleDebitExpense = findDebitExpenseAdapter.findById(expenseId)

        verify(exactly = 1) { debitExpenseRepository.findByIdOrNull(expenseId) }

        assertEquals(debitExpense, possibleDebitExpense)
    }

    @Test
    fun `should not find debit expense by id`() {
        val expenseId = 7L

        every { debitExpenseRepository.findByIdOrNull(expenseId) } returns null

        assertNull(findDebitExpenseAdapter.findById(expenseId))

        verify(exactly = 1) { debitExpenseRepository.findByIdOrNull(expenseId) }
    }

    @Test
    fun `should find by debit expenses debit payment method id`() {
        val debitPaymentMethodId = 10L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)
        val debitExpensesList = DebitExpenseFixtures.getDebitExpenseList(debitPaymentMethod)
        val debitExpenseEntitiesList = debitExpensesList.map { DebitExpenseEntity.fromDomain(it) }

        every { debitExpenseRepository.findByPaymentMethodId(debitPaymentMethodId) } returns debitExpenseEntitiesList

        val possibleDebitExpenses = findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId)

        verify(exactly = 1) { debitExpenseRepository.findByPaymentMethodId(debitPaymentMethodId) }

        assertEquals(debitExpensesList, possibleDebitExpenses)
    }

    @Test
    fun `should not find any debit expense by payment method id`() {
        val debitPaymentMethodId = 10L

        every { debitExpenseRepository.findByPaymentMethodId(debitPaymentMethodId) } returns emptyList()

        assertTrue(findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId).isEmpty())

        verify(exactly = 1) { debitExpenseRepository.findByPaymentMethodId(debitPaymentMethodId) }
    }
}
