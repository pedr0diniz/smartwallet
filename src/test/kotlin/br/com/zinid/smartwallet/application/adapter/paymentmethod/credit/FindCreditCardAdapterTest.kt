package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardRepository
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.FindCreditCardAdapter
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class FindCreditCardAdapterTest {

    private val creditCardRepository = mockk<CreditCardRepository>()
    private val findCreditExpenseAdapter = mockk<FindCreditExpenseOutputPort>()
    private val findCreditCardAdapter = FindCreditCardAdapter(
        creditCardRepository,
        findCreditExpenseAdapter
    )

    @Test
    fun `should find credit card with credit expenses by id`() {
        val creditCardId = 1L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        every { creditCardRepository.findByIdOrNull(creditCardId) } returns creditCardEntity
        every { findCreditExpenseAdapter.findByCreditCardId(creditCardId) } returns creditExpenses

        val possibleCreditCard = findCreditCardAdapter.findById(creditCardId)
        val expectedResponse = creditCard.copy(expenses = creditExpenses)

        verify(exactly = 1) { creditCardRepository.findByIdOrNull(creditCardId) }
        verify(exactly = 1) { findCreditExpenseAdapter.findByCreditCardId(creditCardId) }

        assertEquals(expectedResponse, possibleCreditCard)
    }

    @Test
    fun `should find credit card without credit expenses by id`() {
        val creditCardId = 1L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)

        every { creditCardRepository.findByIdOrNull(creditCardId) } returns creditCardEntity
        every { findCreditExpenseAdapter.findByCreditCardId(creditCardId) } returns emptyList()

        val possibleCreditCard = findCreditCardAdapter.findById(creditCardId)

        verify(exactly = 1) { creditCardRepository.findByIdOrNull(creditCardId) }
        verify(exactly = 1) { findCreditExpenseAdapter.findByCreditCardId(creditCardId) }

        assertEquals(creditCard, possibleCreditCard)
    }

    @Test
    fun `should not find credit card by id`() {
        val creditCardId = 1L

        every { creditCardRepository.findByIdOrNull(creditCardId) } returns null

        assertNull(findCreditCardAdapter.findById(creditCardId))

        verify(exactly = 1) { creditCardRepository.findByIdOrNull(creditCardId) }
    }

    @Test
    fun `should find credit card with expenses by financialAccountId`() {
        val creditCardId = 1L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)
        val creditExpenses = CreditExpenseFixtures.getCreditExpenseList(creditCard)

        val financialAccountId = creditCard.financialAccount.id

        every { creditCardRepository.findByFinancialAccountId(financialAccountId) } returns listOf(creditCardEntity)
        every { findCreditExpenseAdapter.findByCreditCardId(creditCardEntity.id!!) } returns creditExpenses

        val possibleCreditCards = findCreditCardAdapter.findByFinancialAccountId(financialAccountId)
        val expectedResponse = creditCard.copy(expenses = creditExpenses)

        verify(exactly = 1) { creditCardRepository.findByFinancialAccountId(financialAccountId) }
        verify(exactly = 1) { findCreditExpenseAdapter.findByCreditCardId(creditCardId) }

        assertEquals(expectedResponse, possibleCreditCards.first())
    }

    @Test
    fun `should find credit card with no credit expenses by financialAccountId`() {
        val creditCardId = 1L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)

        val financialAccountId = creditCard.financialAccount.id

        every { creditCardRepository.findByFinancialAccountId(financialAccountId) } returns listOf(creditCardEntity)
        every { findCreditExpenseAdapter.findByCreditCardId(creditCardEntity.id!!) } returns emptyList()

        val possibleCreditCards = findCreditCardAdapter.findByFinancialAccountId(financialAccountId)

        verify(exactly = 1) { creditCardRepository.findByFinancialAccountId(financialAccountId) }
        verify(exactly = 1) { findCreditExpenseAdapter.findByCreditCardId(creditCardId) }

        assertEquals(creditCard, possibleCreditCards.first())
    }

    @Test
    fun `should not find credit card by financialAccountId`() {
        val financialAccountId = 1L

        every { creditCardRepository.findByFinancialAccountId(financialAccountId) } returns emptyList()

        assert(findCreditCardAdapter.findByFinancialAccountId(financialAccountId).isEmpty())

        verify(exactly = 1) { creditCardRepository.findByFinancialAccountId(financialAccountId) }
    }
}
