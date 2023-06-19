package br.com.zinid.smartwallet.application.adapter.paymentmethod

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodEntity
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class FindPaymentMethodAdapterTest {

    private val findCreditCardAdapter = mockk<FindCreditCardOutputPort>()
    private val findDebitPaymentMethodAdapter = mockk<FindDebitPaymentMethodOutputPort>()
    private val paymentMethodRepository = mockk<PaymentMethodRepository>()
    private val findPaymentMethodAdapter = FindPaymentMethodAdapter(
        findCreditCardAdapter,
        findDebitPaymentMethodAdapter,
        paymentMethodRepository
    )

    @Test
    fun `should find credit card by id`() {
        val paymentMethodId = 6L
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)
        val paymentMethodEntity = PaymentMethodEntity(
            id = paymentMethodId,
            creditCard = creditCardEntity,
            financialAccount = creditCardEntity.financialAccount
        )

        every { paymentMethodRepository.findByIdOrNull(paymentMethodId) } returns paymentMethodEntity

        val possiblePaymentMethod = findPaymentMethodAdapter.findById(paymentMethodId)

        verify(exactly = 1) { paymentMethodRepository.findByIdOrNull(paymentMethodId) }

        assertTrue(possiblePaymentMethod is CreditCard)
        assertEquals(creditCard, possiblePaymentMethod)
    }

    @Test
    fun `should find debit payment method by id`() {
        val paymentMethodId = 6L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)
        val paymentMethodEntity = PaymentMethodEntity(
            id = paymentMethodId,
            debitPaymentMethod = debitPaymentMethodEntity,
            financialAccount = debitPaymentMethodEntity.financialAccount
        )

        every { paymentMethodRepository.findByIdOrNull(paymentMethodId) } returns paymentMethodEntity

        val possiblePaymentMethod = findPaymentMethodAdapter.findById(paymentMethodId)

        verify(exactly = 1) { paymentMethodRepository.findByIdOrNull(paymentMethodId) }

        assertTrue(possiblePaymentMethod is DebitPaymentMethod)
        assertEquals(debitPaymentMethod, possiblePaymentMethod)
    }

    @Test
    fun `should not find a payment method by id`() {
        val paymentMethodId = 6L

        every { paymentMethodRepository.findByIdOrNull(paymentMethodId) } returns null

        assertNull(findPaymentMethodAdapter.findById(paymentMethodId))

        verify(exactly = 1) { paymentMethodRepository.findByIdOrNull(paymentMethodId) }
    }

    @Test
    fun `should find a credit card by credit card id`() {
        val creditCardId = 5L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)

        every { findCreditCardAdapter.findById(creditCardId) } returns creditCard

        val possiblePaymentMethod = findPaymentMethodAdapter.findByCreditCardId(creditCardId)

        verify(exactly = 1) { findCreditCardAdapter.findById(creditCardId) }

        assertEquals(creditCard, possiblePaymentMethod)
    }

    @Test
    fun `should not find a credit card by credit card id`() {
        val creditCardId = 5L

        every { findCreditCardAdapter.findById(creditCardId) } returns null

        assertNull(findPaymentMethodAdapter.findByCreditCardId(creditCardId))

        verify(exactly = 1) { findCreditCardAdapter.findById(creditCardId) }
    }

    @Test
    fun `should find a debit payment method by debit payment method id`() {
        val debitPaymentMethodId = 4L
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(id = debitPaymentMethodId)

        every { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) } returns debitPaymentMethod

        val possiblePaymentMethod = findPaymentMethodAdapter.findByDebitPaymentMethodId(debitPaymentMethodId)

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) }

        assertEquals(debitPaymentMethod, possiblePaymentMethod)
    }

    @Test
    fun `should not find a debit payment method by debit payment method id`() {
        val debitPaymentMethodId = 4L

        every { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) } returns null

        assertNull(findPaymentMethodAdapter.findByDebitPaymentMethodId(debitPaymentMethodId))

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) }
    }

    @Test
    fun `should find multiple payment methods by financial account id`() {
        val financialAccountId = 8L
        val creditCard = CreditCardFixtures.getCreditCard()
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()

        every {
            findCreditCardAdapter.findByFinancialAccountId(financialAccountId)
        } returns listOf(creditCard)
        every {
            findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)
        } returns listOf(debitPaymentMethod)

        val paymentMethods = findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)
        val expectedResponse = listOf(creditCard, debitPaymentMethod)

        assertEquals(expectedResponse, paymentMethods)
    }

    @Test
    fun `should find no payment methods by financial account id`() {
        val financialAccountId = 8L

        every { findCreditCardAdapter.findByFinancialAccountId(financialAccountId) } returns emptyList()
        every { findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns emptyList()

        assertTrue(findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId).isEmpty())

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) }
    }
}
