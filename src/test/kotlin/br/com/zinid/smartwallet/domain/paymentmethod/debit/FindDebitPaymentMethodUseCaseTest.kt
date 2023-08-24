package br.com.zinid.smartwallet.domain.paymentmethod.debit

import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.FindDebitPaymentMethodUseCase
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindDebitPaymentMethodUseCaseTest {

    private val findDebitPaymentMethodAdapter = mockk<FindDebitPaymentMethodOutputPort>()
    private val findDebitPaymentMethodUseCase = FindDebitPaymentMethodUseCase(findDebitPaymentMethodAdapter)
    private val financialAccountId = 1L

    @Test
    fun `should find debit payment methods by financialAccountId`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val paymentMethods = listOf(PaymentMethodFixtures.getDebitPaymentMethod(financialAccount))
        every { findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns paymentMethods

        val response = findDebitPaymentMethodUseCase.findByFinancialAccountId(financialAccountId)

        assertEquals(paymentMethods, response)
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `should not find debit payment methods by financialAccountId`() {
        every { findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns emptyList()

        assertTrue(findDebitPaymentMethodUseCase.findByFinancialAccountId(financialAccountId).isEmpty())
    }
}
