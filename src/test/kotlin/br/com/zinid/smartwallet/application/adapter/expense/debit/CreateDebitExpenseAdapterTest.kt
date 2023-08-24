package br.com.zinid.smartwallet.application.adapter.expense.debit

import br.com.zinid.smartwallet.application.adapter.expense.ExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.ExpenseRepository
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.CreateDebitExpenseAdapter
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseRepository
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateDebitExpenseAdapterTest {

    private val debitExpenseRepository = mockk<DebitExpenseRepository>()
    private val expenseRepository = mockk<ExpenseRepository>()
    private val createDebitExpenseAdapter = CreateDebitExpenseAdapter(
        debitExpenseRepository,
        expenseRepository
    )

    @Test
    fun `should create debit expense`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpense = DebitExpenseFixtures.getElectricalPowerBillDebitExpense(debitPaymentMethod)
        val debitExpenseEntity = DebitExpenseEntity.fromDomain(debitExpense)
        val expenseEntity = ExpenseEntity.from(debitExpenseEntity)

        every { debitExpenseRepository.save(debitExpenseEntity) } returns debitExpenseEntity
        every { expenseRepository.save(any()) } returns expenseEntity

        val createdDebitExpense = createDebitExpenseAdapter.create(debitExpense)

        assertEquals(debitExpense, createdDebitExpense)

        verify(exactly = 1) { debitExpenseRepository.save(debitExpenseEntity) }
        verify(exactly = 1) { expenseRepository.save(any()) }
    }
}
