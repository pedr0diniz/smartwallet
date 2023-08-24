package br.com.zinid.smartwallet.application.adapter.expense.credit

import br.com.zinid.smartwallet.application.adapter.expense.ExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.ExpenseRepository
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreateCreditExpenseAdapter
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseRepository
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import br.com.zinid.smartwallet.fixtures.CreditExpenseFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateCreditExpenseAdapterTest {

    private val creditExpenseRepository = mockk<CreditExpenseRepository>()
    private val expenseRepository = mockk<ExpenseRepository>()
    private val createCreditExpenseAdapter = CreateCreditExpenseAdapter(
        creditExpenseRepository,
        expenseRepository
    )

    @Test
    fun `should create credit expense`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditExpense = CreditExpenseFixtures.getFoodDeliveryCreditExpense(creditCard)
        val creditExpenseEntity = CreditExpenseEntity.fromDomain(creditExpense)
        val expenseEntity = ExpenseEntity.from(creditExpenseEntity)

        every { creditExpenseRepository.save(creditExpenseEntity) } returns creditExpenseEntity
        every { expenseRepository.save(any()) } returns expenseEntity

        val createdCreditExpense = createCreditExpenseAdapter.create(creditExpense)

        assertEquals(creditExpense, createdCreditExpense)

        verify(exactly = 1) { creditExpenseRepository.save(creditExpenseEntity) }
        verify(exactly = 1) { expenseRepository.save(any()) }
    }
}
