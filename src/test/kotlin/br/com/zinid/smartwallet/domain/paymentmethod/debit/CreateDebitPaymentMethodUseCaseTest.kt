package br.com.zinid.smartwallet.domain.paymentmethod.debit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.CreateDebitPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.CreateDebitPaymentMethodUseCase
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class CreateDebitPaymentMethodUseCaseTest {

    private val findFinancialAccountAdapter = mockk<FindFinancialAccountOutputPort>()
    private val createDebitPaymentMethodAdapter = mockk<CreateDebitPaymentMethodAdapter>()
    private val createDebitPaymentMethodUseCase = CreateDebitPaymentMethodUseCase(
        findFinancialAccountAdapter,
        createDebitPaymentMethodAdapter
    )

    @Test
    fun `should find financial account and create debit payment method`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val financialAccountId = financialAccount.id!!

        every {
            findFinancialAccountAdapter.findById(financialAccountId)
        } returns financialAccount
        every { createDebitPaymentMethodAdapter.create(debitPaymentMethod) } returns debitPaymentMethod

        assertEquals(debitPaymentMethod, createDebitPaymentMethodUseCase.execute(debitPaymentMethod))

        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
        verify(exactly = 1) { createDebitPaymentMethodAdapter.create(debitPaymentMethod) }
    }

    @Test
    fun `should not find financial account and not create debit payment method`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val financialAccountId = financialAccount.id!!

        every { findFinancialAccountAdapter.findById(financialAccountId) } returns null

        assertThrows<NotFoundException> { createDebitPaymentMethodUseCase.execute(debitPaymentMethod) }

        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
    }
}
