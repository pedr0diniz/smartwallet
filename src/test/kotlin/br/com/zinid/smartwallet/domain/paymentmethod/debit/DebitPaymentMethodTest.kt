package br.com.zinid.smartwallet.domain.paymentmethod.debit

import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
import br.com.zinid.smartwallet.domain.exception.InvalidDateRangeException
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.fixtures.DebitExpenseFixtures
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
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
    fun `should create blank debit payment method from id`() {
        val id = 9L
        val debitPaymentMethod = DebitPaymentMethod.createBlankFromId(id)

        assertEquals(id, debitPaymentMethod.id)
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
        val expense = DebitExpenseFixtures.getWaterBillDebitExpense(debitPaymentMethod)
            .copy(price = BigDecimal.valueOf(1500.00))

        assertTrue(debitPaymentMethod.canPurchase(expense))
    }

    @Test
    fun `should not be able to purchase`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.valueOf(1000.00),
            overdraft = BigDecimal.valueOf(500.00)
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val expense = DebitExpenseFixtures.getWaterBillDebitExpense(debitPaymentMethod)
            .copy(price = BigDecimal.valueOf(1500.01))

        assertFalse(debitPaymentMethod.canPurchase(expense))
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
        assertEquals(expenses.sumOf { it.price }, debitPaymentMethod.getMonthlyExpensesValue(YearMonth.now()))
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
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod().copy(expenses = emptyList())

        assertTrue(debitPaymentMethod.getMonthlyExpenses(YearMonth.now()).isEmpty())
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

        assertThrows<InvalidDateRangeException> {
            debitPaymentMethod.getExpensesWithinDateRange(
                LocalDate.MAX,
                LocalDate.now()
            )
        }
    }

    @Test
    fun `should process expense`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.valueOf(1000.00)
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val debitExpense = DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod)

        val expectedBalance = financialAccount.balance.minus(debitExpense.price)

        assertDoesNotThrow { debitPaymentMethod.processExpense(debitExpense) }

        assertEquals(expectedBalance, debitPaymentMethod.financialAccount.balance)
    }

    @Test
    fun `should not process expense`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount().copy(
            balance = BigDecimal.ZERO,
            overdraft = BigDecimal.ZERO
        )
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod(financialAccount)
        val debitExpense = DebitExpenseFixtures.getTelecomBillDebitExpense(debitPaymentMethod)

        assertThrows<InsufficientBalanceException> { debitPaymentMethod.processExpense(debitExpense) }
    }
}
