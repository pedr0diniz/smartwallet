package br.com.zinid.smartwallet.domain.expense.debit

import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    @Test
    fun `should process expense`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.valueOf(1000.00)
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val debitExpense = DebitExpenseFixtures.getElectricalPowerBillDebitExpense(debitPaymentMethod)

        val expectedBalance = financialAccount.balance.minus(debitExpense.price)

        debitExpense.process()

        assertEquals(debitExpense.getFinancialAccount().balance, expectedBalance)
    }

    @Test
    fun `should not process expense`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.ZERO,
            overdraft = BigDecimal.ZERO
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val debitExpense = DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod)

        assertFalse(debitExpense.process())
    }
}
