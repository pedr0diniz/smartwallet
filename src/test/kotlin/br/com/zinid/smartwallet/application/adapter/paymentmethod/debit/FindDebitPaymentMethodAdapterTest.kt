package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodRepository
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.FindDebitPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.expense.debit.output.FindDebitExpenseOutputPort
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class FindDebitPaymentMethodAdapterTest {

    private val debitPaymentMethodRepository = mockk<DebitPaymentMethodRepository>()
    private val findDebitExpenseAdapter = mockk<FindDebitExpenseOutputPort>()
    private val findDebitPaymentMethodAdapter = FindDebitPaymentMethodAdapter(
        debitPaymentMethodRepository,
        findDebitExpenseAdapter
    )

    @Test
    fun `should find debit payment method with debit expenses by id`() {
        val debitPaymentMethodId = 1L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)
        val debitExpenses = DebitExpenseFixtures.getDebitExpenseList(debitPaymentMethod)

        every { debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId) } returns debitPaymentMethodEntity
        every { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId) } returns debitExpenses

        val possibleDebitPaymentMethod = findDebitPaymentMethodAdapter.findById(debitPaymentMethodId)
        val expectedResponse = debitPaymentMethod.copy(expenses = debitExpenses)

        verify(exactly = 1) { debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId) }
        verify(exactly = 1) { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId) }

        assertEquals(expectedResponse, possibleDebitPaymentMethod)
    }

    @Test
    fun `should find debit payment method without debit expenses by id`() {
        val debitPaymentMethodId = 1L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)

        every { debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId) } returns debitPaymentMethodEntity
        every { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId) } returns emptyList()

        val possibleDebitPaymentMethod = findDebitPaymentMethodAdapter.findById(debitPaymentMethodId)

        verify(exactly = 1) { debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId) }
        verify(exactly = 1) { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId) }

        assertEquals(debitPaymentMethod, possibleDebitPaymentMethod)
    }

    @Test
    fun `should not find debit payment method by id`() {
        val debitPaymentMethodId = 1L

        every { debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId) } returns null

        assertNull(findDebitPaymentMethodAdapter.findById(debitPaymentMethodId))

        verify(exactly = 1) { debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId) }
    }

    @Test
    fun `should find debit payment method with expenses by financialAccountId`() {
        val debitPaymentMethodId = 1L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)
        val debitExpenses = DebitExpenseFixtures.getDebitExpenseList(debitPaymentMethod)

        val financialAccountId = debitPaymentMethod.financialAccount.id!!

        every { debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId) } returns listOf(debitPaymentMethodEntity)
        every { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodEntity.id!!) } returns debitExpenses

        val possibleDebitPaymentMethods = findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)
        val expectedResponse = debitPaymentMethod.copy(expenses = debitExpenses)

        verify(exactly = 1) { debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId) }
        verify(exactly = 1) { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId) }

        assertEquals(expectedResponse, possibleDebitPaymentMethods.first())
    }

    @Test
    fun `should find debit payment method with no debit expenses by financialAccountId`() {
        val debitPaymentMethodId = 1L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)

        val financialAccountId = debitPaymentMethod.financialAccount.id!!

        every { debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId) } returns listOf(debitPaymentMethodEntity)
        every { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodEntity.id!!) } returns emptyList()

        val possibleDebitPaymentMethods = findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)

        verify(exactly = 1) { debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId) }
        verify(exactly = 1) { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId) }

        assertEquals(debitPaymentMethod, possibleDebitPaymentMethods.first())
    }

    @Test
    fun `should not find debit payment method by financialAccountId`() {
        val financialAccountId = 1L

        every { debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId) } returns emptyList()

        assert(findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId).isEmpty())

        verify(exactly = 1) { debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId) }
    }
}
