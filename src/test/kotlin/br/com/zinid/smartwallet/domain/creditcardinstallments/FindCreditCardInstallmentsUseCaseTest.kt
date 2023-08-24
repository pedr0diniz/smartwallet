package br.com.zinid.smartwallet.domain.creditcardinstallments

import br.com.zinid.smartwallet.domain.creditcardinstallment.input.FindCreditCardInstallmentsUseCase
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.FindCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FindCreditCardInstallmentsUseCaseTest {

    private val findCreditExpenseAdapter = mockk<FindCreditExpenseOutputPort>()
    private val findCreditCardInstallmentsAdapter = mockk<FindCreditCardInstallmentsOutputPort>()
    private val findCreditCardInstallmentsUseCase = FindCreditCardInstallmentsUseCase(
        findCreditExpenseAdapter,
        findCreditCardInstallmentsAdapter
    )
    private val creditExpenseId = 1L

    @Test
    fun `should find credit card installments by creditExpenseId`() {
        val creditCard = PaymentMethodFixtures.getCreditPaymentMethod(FinancialAccountFixtures.getFinancialAccount())
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        every { findCreditExpenseAdapter.findById(creditExpenseId) } returns creditExpense
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(creditExpenseId) } returns creditCardInstallments

        val response = findCreditCardInstallmentsUseCase.findByCreditExpenseId(creditExpenseId)

        assertNotNull(response)
        assertEquals(creditCardInstallments, response)
    }

    @Test
    fun `should not find credit card installments by creditExpenseId`() {
        val creditCard = PaymentMethodFixtures.getCreditPaymentMethod(FinancialAccountFixtures.getFinancialAccount())
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)

        every { findCreditExpenseAdapter.findById(creditExpenseId) } returns creditExpense
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(creditExpenseId) } returns null

        assertNull(findCreditCardInstallmentsUseCase.findByCreditExpenseId(creditExpenseId))
    }

    @Test
    fun `should throw exception when credit expense cannot be found by its id`() {
        every { findCreditExpenseAdapter.findById(creditExpenseId) } returns null

        assertThrows<NotFoundException> { findCreditCardInstallmentsUseCase.findByCreditExpenseId(creditExpenseId) }
    }
}
