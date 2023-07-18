package br.com.zinid.smartwallet.application.adapter.expense.credit

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreditCardInstallmentsRepository
import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.FindCreditCardInstallmentsAdapter
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseRepository
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.FindCreditExpenseAdapter
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class FindCreditExpenseAdapterTest {

    private val creditExpenseRepository = mockk<CreditExpenseRepository>()
    private val creditCardInstallmentsRepository = mockk<CreditCardInstallmentsRepository>()
    private val findCreditCardInstallmentsAdapter = FindCreditCardInstallmentsAdapter(creditCardInstallmentsRepository)
    private val findCreditExpenseAdapter = FindCreditExpenseAdapter(
        creditExpenseRepository,
        findCreditCardInstallmentsAdapter
    )

    @Test
    fun `should find credit expense with installments by id`() {
        val expenseId = 8L
        val creditCardId = 13L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditExpense = CreditExpenseFixtures.getVacuumCleanerCreditExpenseWithInstallments(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val creditExpenseEntity = CreditExpenseEntity.fromDomain(creditExpense)

        every { creditExpenseRepository.findByIdOrNull(expenseId) } returns creditExpenseEntity
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(expenseId) } returns creditCardInstallments

        val possibleCreditExpense = findCreditExpenseAdapter.findById(expenseId)

        verify(exactly = 1) { creditExpenseRepository.findByIdOrNull(expenseId) }

        assertEquals(creditExpense, possibleCreditExpense)
    }

    @Test
    fun `should find credit expense without installments by id`() {
        val expenseId = 8L
        val creditCardId = 13L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)
        val creditCardInstallments = creditExpense.creditCardInstallments

        val creditExpenseEntity = CreditExpenseEntity.fromDomain(creditExpense)

        every { creditExpenseRepository.findByIdOrNull(expenseId) } returns creditExpenseEntity
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(expenseId) } returns creditCardInstallments

        val possibleCreditExpense = findCreditExpenseAdapter.findById(expenseId)

        verify(exactly = 1) { creditExpenseRepository.findByIdOrNull(expenseId) }

        assertEquals(creditExpense, possibleCreditExpense)
    }

    @Test
    fun `should not find credit expense by id`() {
        val expenseId = 8L

        every { creditExpenseRepository.findByIdOrNull(expenseId) } returns null

        assertNull(findCreditExpenseAdapter.findById(expenseId))

        verify(exactly = 1) { creditExpenseRepository.findByIdOrNull(expenseId) }
    }

    @Test
    fun `should find credit expense with installments by credit card id`() {
        val creditCardId = 13L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditExpensesList = CreditExpenseFixtures.getCreditExpenseList(creditCard)
        val creditCardInstallments = creditExpensesList.map { it.creditCardInstallments }.first()

        val creditExpenseEntitiesList = creditExpensesList.map { CreditExpenseEntity.fromDomain(it) }

        every { creditExpenseRepository.findByPaymentMethodId(creditCardId) } returns creditExpenseEntitiesList
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(3L) } returns creditCardInstallments
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(4L) } returns null
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(5L) } returns null

        val possibleCreditExpenses = findCreditExpenseAdapter.findByCreditCardId(creditCardId)

        verify(exactly = 1) { creditExpenseRepository.findByPaymentMethodId(creditCardId) }
        verify(exactly = 1) { findCreditCardInstallmentsAdapter.findByCreditExpenseId(3L) }
        verify(exactly = 1) { findCreditCardInstallmentsAdapter.findByCreditExpenseId(4L) }
        verify(exactly = 1) { findCreditCardInstallmentsAdapter.findByCreditExpenseId(5L) }

        assertEquals(creditExpensesList, possibleCreditExpenses)
    }

    @Test
    fun `should find credit expense without installments by credit card id`() {
        val creditCardId = 13L
        val creditCard = CreditCardFixtures.getCreditCard().copy(id = creditCardId)
        val creditExpensesList = listOf(
            CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard),
            CreditExpenseFixtures.getSubscriptionCreditExpense(creditCard)
        )

        val creditExpenseEntitiesList = creditExpensesList.map { CreditExpenseEntity.fromDomain(it) }

        every { creditExpenseRepository.findByPaymentMethodId(creditCardId) } returns creditExpenseEntitiesList
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(4L) } returns null
        every { findCreditCardInstallmentsAdapter.findByCreditExpenseId(5L) } returns null

        val possibleCreditExpenses = findCreditExpenseAdapter.findByCreditCardId(creditCardId)

        verify(exactly = 1) { creditExpenseRepository.findByPaymentMethodId(creditCardId) }
        verify(exactly = 1) { findCreditCardInstallmentsAdapter.findByCreditExpenseId(4L) }
        verify(exactly = 1) { findCreditCardInstallmentsAdapter.findByCreditExpenseId(5L) }

        assertEquals(creditExpensesList, possibleCreditExpenses)
    }

    @Test
    fun `should not find credit expense by credit card id`() {
        val creditCardId = 13L

        every { creditExpenseRepository.findByPaymentMethodId(creditCardId) } returns emptyList()

        assertTrue(findCreditExpenseAdapter.findByCreditCardId(creditCardId).isEmpty())

        verify(exactly = 1) { creditExpenseRepository.findByPaymentMethodId(creditCardId) }
    }
}
