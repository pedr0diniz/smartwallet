package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.input.CreateCreditExpenseUseCase
import br.com.zinid.smartwallet.domain.expense.credit.output.CreateCreditExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CreateCreditExpenseUseCaseTest {
    private val findCreditCardAdapter = mockk<FindCreditCardOutputPort>()
    private val createCreditCardInstallmentsAdapter = mockk<CreateCreditCardInstallmentsOutputPort>()
    private val createCreditExpenseAdapter = mockk<CreateCreditExpenseOutputPort>()
    private val createCreditExpenseUseCase = CreateCreditExpenseUseCase(
        findCreditCardAdapter,
        createCreditCardInstallmentsAdapter,
        createCreditExpenseAdapter
    )

    @Test
    fun `should find credit card and create credit expense`() {
        val creditCardId = 9L
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            id = creditCardId,
            cardLimit = BigDecimal.valueOf(10000L)
        )
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        every { findCreditCardAdapter.findById(creditCardId) } returns creditCard
        every { createCreditExpenseAdapter.create(creditExpense) } returns creditExpense
        every { createCreditCardInstallmentsAdapter.createFromExpense(creditExpense) } returns creditExpense.creditCardInstallments

        val possibleExpense = createCreditExpenseUseCase.execute(creditExpense)

        verify(exactly = 1) { findCreditCardAdapter.findById(creditCardId) }
        verify(exactly = 1) { createCreditExpenseAdapter.create(creditExpense) }
        verify(exactly = 1) { createCreditCardInstallmentsAdapter.createFromExpense(creditExpense) }

        assertEquals(creditExpense, possibleExpense)
    }

    @Test
    fun `should not find credit card and not create credit expense`() {
        val creditCardId = 9L
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            id = creditCardId,
            cardLimit = BigDecimal.valueOf(10000L)
        )
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpense(creditCard)

        every { findCreditCardAdapter.findById(creditCardId) } returns null

        assertNull(createCreditExpenseUseCase.execute(creditExpense))

        verify(exactly = 1) { findCreditCardAdapter.findById(creditCardId) }
    }

    @Test
    fun `should not create a credit expense when the credit card has no limit`() {
        val creditCardId = 9L
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            id = creditCardId,
            cardLimit = BigDecimal.valueOf(10000L)
        )
        val creditExpense = CreditExpenseFixtures.getRenovationsCreditExpense(creditCard)
            .copy(price = BigDecimal.valueOf(11000L))

        every { findCreditCardAdapter.findById(creditCardId) } returns creditCard

        assertNull(createCreditExpenseUseCase.execute(creditExpense))

        verify(exactly = 1) { findCreditCardAdapter.findById(creditCardId) }
    }

    @Test
    fun `should not create a credit expense when the credit card is expired`() {
        val creditCardId = 9L
        val creditCard = CreditCardFixtures.getCreditCard().copy(
            id = creditCardId,
            cardLimit = BigDecimal.valueOf(10000L),
            expirationDate = LocalDate.now().minusMonths(1L)
        )
        val creditExpense = CreditExpenseFixtures.getRenovationsCreditExpense(creditCard)
            .copy(price = BigDecimal.valueOf(9000L))

        every { findCreditCardAdapter.findById(creditCardId) } returns creditCard

        assertNull(createCreditExpenseUseCase.execute(creditExpense))

        verify(exactly = 1) { findCreditCardAdapter.findById(creditCardId) }
    }
}
