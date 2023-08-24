package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.expense.credit.input.FindCreditExpenseUseCase
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindCreditExpenseUseCaseTest {

    private val findCreditExpenseAdapter = mockk<FindCreditExpenseOutputPort>()
    private val findCreditExpenseUseCase = FindCreditExpenseUseCase(findCreditExpenseAdapter)
    private val creditExpenseId = 1L

    @Test
    fun `should find credit expenses by creditCardId`() {
        val creditCard = PaymentMethodFixtures.getCreditPaymentMethod(FinancialAccountFixtures.getFinancialAccount())
        val creditExpenses = listOf(CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard))
        every { findCreditExpenseAdapter.findByCreditCardId(creditExpenseId) } returns creditExpenses

        val response = findCreditExpenseUseCase.findByCreditCardId(creditExpenseId)

        assertEquals(creditExpenses, response)
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `should not find credit expenses by creditCardId`() {
        every { findCreditExpenseAdapter.findByCreditCardId(creditExpenseId) } returns emptyList()

        assertTrue(findCreditExpenseUseCase.findByCreditCardId(creditExpenseId).isEmpty())
    }
}
