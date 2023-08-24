package br.com.zinid.smartwallet.domain.expense.debit

import br.com.zinid.smartwallet.domain.expense.debit.input.FindDebitExpenseUseCase
import br.com.zinid.smartwallet.domain.expense.debit.output.FindDebitExpenseOutputPort
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindDebitExpenseUseCaseTest {

    private val findDebitExpenseAdapter = mockk<FindDebitExpenseOutputPort>()
    private val findDebitExpenseUseCase = FindDebitExpenseUseCase(findDebitExpenseAdapter)
    private val debitPaymentExpenseId = 1L

    @Test
    fun `should find debit expenses by debitPaymentMethodId`() {
        val debitPaymentMethod = PaymentMethodFixtures.getDebitPaymentMethod(FinancialAccountFixtures.getFinancialAccount())
        val debitPaymentExpenses = listOf(DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod))
        every { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentExpenseId) } returns debitPaymentExpenses

        val response = findDebitExpenseUseCase.findByDebitPaymentMethodId(debitPaymentExpenseId)

        assertEquals(debitPaymentExpenses, response)
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `should not find debit expenses by debitPaymentMethodId`() {
        every { findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentExpenseId) } returns emptyList()

        assertTrue(findDebitExpenseUseCase.findByDebitPaymentMethodId(debitPaymentExpenseId).isEmpty())
    }
}
