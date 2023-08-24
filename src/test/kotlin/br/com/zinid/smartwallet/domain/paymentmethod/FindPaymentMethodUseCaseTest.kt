package br.com.zinid.smartwallet.domain.paymentmethod

import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindPaymentMethodUseCaseTest {

    private val findPaymentMethodAdapter = mockk<FindPaymentMethodOutputPort>()
    private val findPaymentMethodUseCase = FindPaymentMethodUseCase(findPaymentMethodAdapter)
    private val financialAccountId = 1L

    @Test
    fun `should find payment methods by financialAccountId`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val paymentMethods = listOf(
            PaymentMethodFixtures.getDebitPaymentMethod(financialAccount),
            PaymentMethodFixtures.getCreditPaymentMethod(financialAccount)
        )
        every { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns paymentMethods

        val response = findPaymentMethodUseCase.findByFinancialAccountId(financialAccountId)

        assertEquals(paymentMethods, response)
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `should not find payment methods by financialAccountId`() {
        every { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns emptyList()

        assertTrue(findPaymentMethodUseCase.findByFinancialAccountId(financialAccountId).isEmpty())
    }
}
