package br.com.zinid.smartwallet.domain.expense.debit

import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.expense.debit.input.CreateDebitExpenseUseCase
import br.com.zinid.smartwallet.domain.expense.debit.output.CreateDebitExpenseOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.UpdateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

internal class CreateDebitExpenseUseCaseTest {

    private val findDebitPaymentMethodAdapter = mockk<FindDebitPaymentMethodOutputPort>()
    private val findFinancialAccountAdapter = mockk<FindFinancialAccountOutputPort>()
    private val updateFinancialAccountAdapter = mockk<UpdateFinancialAccountOutputPort>()
    private val createDebitExpenseAdapter = mockk<CreateDebitExpenseOutputPort>()
    private val createDebitExpenseUseCase = CreateDebitExpenseUseCase(
        findDebitPaymentMethodAdapter,
        findFinancialAccountAdapter,
        updateFinancialAccountAdapter,
        createDebitExpenseAdapter
    )

    @Test
    fun `should create debit expense`() {
        val financialAccountId = 3L
        val debitPaymentMethodId = 11L
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
            .copy(
                id = financialAccountId,
                balance = BigDecimal.valueOf(1000.00)
            )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
            .copy(id = debitPaymentMethodId)
        val originalExpense = DebitExpenseFixtures.getExpenseWithBlankPaymentMethod(debitPaymentMethodId)
        val enrichedExpense = originalExpense.copy(
            paymentMethod = debitPaymentMethod.copy(
                financialAccount = financialAccount
            )
        )

        every { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) } returns debitPaymentMethod
        every { findFinancialAccountAdapter.findById(financialAccountId) } returns financialAccount
        every { updateFinancialAccountAdapter.updateByDebitExpense(enrichedExpense) } returns financialAccount
        every { createDebitExpenseAdapter.create(enrichedExpense) } returns enrichedExpense

        val possibleExpense = createDebitExpenseUseCase.execute(originalExpense)

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) }
        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
        verify(exactly = 1) { updateFinancialAccountAdapter.updateByDebitExpense(enrichedExpense) }
        verify(exactly = 1) { createDebitExpenseAdapter.create(enrichedExpense) }

        assertEquals(enrichedExpense, possibleExpense)
    }

    @Test
    fun `should not create debit expense when a payment method can't found`() {
        val debitPaymentMethodId = 11L
        val originalExpense = DebitExpenseFixtures.getExpenseWithBlankPaymentMethod(debitPaymentMethodId)

        every { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) } returns null

        assertThrows<NotFoundException> { createDebitExpenseUseCase.execute(originalExpense) }

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) }
    }

    @Test
    fun `should not create debit expense when a financial account can't be found`() {
        val financialAccountId = 3L
        val debitPaymentMethodId = 11L
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
            .copy(
                id = financialAccountId,
                balance = BigDecimal.valueOf(1000.00)
            )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
            .copy(id = debitPaymentMethodId)
        val originalExpense = DebitExpenseFixtures.getExpenseWithBlankPaymentMethod(debitPaymentMethodId)

        every { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) } returns debitPaymentMethod
        every { findFinancialAccountAdapter.findById(financialAccountId) } returns null

        assertThrows<NotFoundException> { createDebitExpenseUseCase.execute(originalExpense) }

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) }
        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
    }

    @Test
    fun `should not create debit expense when financial account has no balance`() {
        val financialAccountId = 3L
        val debitPaymentMethodId = 11L
        val financialAccount = FinancialAccountFixtures.getFinancialAccount()
            .copy(
                id = financialAccountId,
                balance = BigDecimal.ZERO
            )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
            .copy(id = debitPaymentMethodId)
        val originalExpense = DebitExpenseFixtures.getExpenseWithBlankPaymentMethod(debitPaymentMethodId)

        every { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) } returns debitPaymentMethod
        every { findFinancialAccountAdapter.findById(financialAccountId) } returns financialAccount

        assertThrows<InsufficientBalanceException> { createDebitExpenseUseCase.execute(originalExpense) }

        verify(exactly = 1) { findDebitPaymentMethodAdapter.findById(debitPaymentMethodId) }
        verify(exactly = 1) { findFinancialAccountAdapter.findById(financialAccountId) }
    }
}
