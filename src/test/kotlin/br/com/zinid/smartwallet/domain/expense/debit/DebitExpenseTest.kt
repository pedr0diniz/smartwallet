package br.com.zinid.smartwallet.domain.expense.debit

import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class DebitExpenseTest {

    private val today = LocalDate.now()

    @Test
    fun `should get payment type`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpense = DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod)

        assertEquals(debitPaymentMethod.type, debitExpense.getPaymentType())
    }

    @Test
    fun `should find debit expenses when filtering by date range`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpenses = DebitExpenseFixtures.getDebitExpenseList(debitPaymentMethod)

        val monthlyDebitExpenses = debitExpenses.filterWithinDateRange(
            today.withDayOfMonth(1),
            today.withDayOfMonth(today.lengthOfMonth())
        )

        assertTrue(monthlyDebitExpenses.isNotEmpty())
        assertEquals(monthlyDebitExpenses.size, debitExpenses.size)
    }

    @Test
    fun `should not find any debit expenses when filtering by date range`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpenses = DebitExpenseFixtures.getDebitExpenseList(debitPaymentMethod)

        val monthlyDebitExpenses = debitExpenses.filterWithinDateRange(
            today.plusMonths(1).withDayOfMonth(1),
            today.plusMonths(1).withDayOfMonth(today.plusMonths(1).lengthOfMonth())
        )

        assertTrue(monthlyDebitExpenses.isEmpty())
    }

    @Test
    fun `should throw exception when filtering by invalid date range`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpenses = DebitExpenseFixtures.getDebitExpenseList(debitPaymentMethod)

        assertThrows<IllegalStateException> {
            debitExpenses.filterWithinDateRange(
                today.withDayOfMonth(today.lengthOfMonth()),
                today.withDayOfMonth(1)
            )
        }
    }

    @Test
    fun `should get financial account`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitExpense = DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod)

        assertEquals(debitPaymentMethod.financialAccount, debitExpense.getFinancialAccount())
    }
}
