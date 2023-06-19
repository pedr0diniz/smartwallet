package br.com.zinid.smartwallet.application.adapter.expense.debit

import br.com.zinid.smartwallet.application.adapter.expense.debit.output.CreateDebitExpenseAdapter
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseRepository
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateDebitExpenseAdapterTest {

    private val debitExpenseRepository = mockk<DebitExpenseRepository>()
    private val createDebitExpenseAdapter = CreateDebitExpenseAdapter(debitExpenseRepository)

    @Test
    fun `should create debit expense`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpense = DebitExpenseFixtures.getElectricalPowerBillDebitExpense(debitPaymentMethod)
        val debitExpenseEntity = DebitExpenseEntity.fromDomain(debitExpense)

        every { debitExpenseRepository.save(debitExpenseEntity) } returns debitExpenseEntity

        val createdDebitExpense = createDebitExpenseAdapter.create(debitExpense)

        assertEquals(debitExpense, createdDebitExpense)
    }
}
