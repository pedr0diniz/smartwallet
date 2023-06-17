package br.com.zinid.smartwallet.domain.paymentmethod.debit

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
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

internal class DebitPaymentMethodTest {

    @Test
    fun `should create blank debit payment method`() {
        val debitPaymentMethod = DebitPaymentMethod.createBlank()

        assertEquals(0L, debitPaymentMethod.id)
        assertEquals(PaymentType.BLANK, debitPaymentMethod.type)
        assertEquals(FinancialAccount.createBlank(), debitPaymentMethod.financialAccount)
        assertEquals(emptyList(), debitPaymentMethod.expenses)
    }

    @Test
    fun `should get remaining spendable value`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.valueOf(1000.00),
            overdraft = BigDecimal.valueOf(500.00)
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)

        val remainingSpendableValue = debitPaymentMethod.getRemainingSpendableValue()
        val expectedValue = financialAccount.balance.add(financialAccount.overdraft)

        assertEquals(expectedValue, remainingSpendableValue)
    }

    @Test
    fun `should be able to purchase`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.valueOf(1000.00),
            overdraft = BigDecimal.valueOf(500.00)
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)

        assertTrue(debitPaymentMethod.canPurchase(BigDecimal.valueOf(1500.00)))
    }

    @Test
    fun `should not be able to purchase`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.valueOf(1000.00),
            overdraft = BigDecimal.valueOf(500.00)
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)

        assertFalse(debitPaymentMethod.canPurchase(BigDecimal.valueOf(1500.01)))
    }

    @Test
    fun `should get expenses within date range`() {
        val tempMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitPaymentMethod = tempMethod.copy(expenses = DebitExpenseFixtures.getDebitExpenseList(tempMethod))

        val expenses = debitPaymentMethod.getExpensesWithinDateRange(
            startDate = LocalDate.now().withDayOfMonth(1),
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
        )

        assertEquals(3, expenses.size)
        assertEquals(expenses.sumOf { it.price }, debitPaymentMethod.getMonthlyExpensesValue())
    }

    @Test
    fun `should not find any expense within date range`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()

        val expenses = debitPaymentMethod.getExpensesWithinDateRange(
            startDate = LocalDate.now().withDayOfMonth(1),
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
        )

        assertTrue(expenses.isEmpty())
    }

    @Test
    fun `should not find any monthly expense`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(expenses = null)

        assertTrue(debitPaymentMethod.getMonthlyExpenses().isEmpty())
    }

    @Test
    fun `should get expenses value within date range`() {
        val tempMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitPaymentMethod = tempMethod.copy(expenses = DebitExpenseFixtures.getDebitExpenseList(tempMethod))

        val expensesValue = debitPaymentMethod.getExpensesValueWithinDateRange(
            startDate = LocalDate.now().withDayOfMonth(1),
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
        )

        val expectedValue =
            DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod).price
                .add(DebitExpenseFixtures.getWaterBillDebitExpense(debitPaymentMethod).price)
                .add(DebitExpenseFixtures.getElectricalPowerBillDebitExpense(debitPaymentMethod).price)

        assertEquals(expectedValue, expensesValue)
    }

    @Test
    fun `should not get any expenses if start date is after end date`() {
        val tempMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitPaymentMethod = tempMethod.copy(expenses = DebitExpenseFixtures.getDebitExpenseList(tempMethod))

        assertThrows<IllegalStateException> {
            debitPaymentMethod.getExpensesWithinDateRange(
                LocalDate.MAX,
                LocalDate.now()
            )
        }
    }
}
