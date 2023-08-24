package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.FindCreditCardUseCase
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindCreditCardUseCaseTest {

    private val findCreditCardAdapter = mockk<FindCreditCardOutputPort>()
    private val findCreditCardUseCase = FindCreditCardUseCase(findCreditCardAdapter)
    private val financialAccountId = 1L

    @Test
    fun `should find credit cards by financialAccountId`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val paymentMethods = listOf(PaymentMethodFixtures.getCreditPaymentMethod(financialAccount))
        every { findCreditCardAdapter.findByFinancialAccountId(financialAccountId) } returns paymentMethods

        val response = findCreditCardUseCase.findByFinancialAccountId(financialAccountId)

        assertEquals(paymentMethods, response)
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `should not find credit cards by financialAccountId`() {
        every { findCreditCardAdapter.findByFinancialAccountId(financialAccountId) } returns emptyList()

        assertTrue(findCreditCardUseCase.findByFinancialAccountId(financialAccountId).isEmpty())
    }
}
