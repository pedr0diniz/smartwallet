package br.com.zinid.smartwallet.domain.paymentmethod.credit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreateCreditCardAdapter
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.CreateCreditCardUseCase
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class CreateCreditCardUseCaseTest {

    private val findFinancialAccountAdapter = mockk<FindFinancialAccountOutputPort>()
    private val createCreditCardAdapter = mockk<CreateCreditCardAdapter>()
    private val createCreditCardUseCase = CreateCreditCardUseCase(
        findFinancialAccountAdapter,
        createCreditCardAdapter
    )

    @Test
    fun `should find financial account and create credit card`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val creditCard = CreditCardFixtures.getCreditCard(financialAccount)
        val financialAccountId = financialAccount.id!!

        every {
            findFinancialAccountAdapter.findById(financialAccountId)
        } returns financialAccount
        every { createCreditCardAdapter.create(creditCard) } returns creditCard

        assertEquals(creditCard, createCreditCardUseCase.execute(creditCard))

        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
        verify(exactly = 1) { createCreditCardAdapter.create(creditCard) }
    }

    @Test
    fun `should not find financial account and not create credit card`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val creditCard = CreditCardFixtures.getCreditCard(financialAccount)
        val financialAccountId = financialAccount.id!!

        every { findFinancialAccountAdapter.findById(financialAccountId) } returns null

        assertThrows<NotFoundException> { createCreditCardUseCase.execute(creditCard) }

        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
    }
}
