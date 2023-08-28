package br.com.zinid.smartwallet.domain.paymentmethod.debit

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.expense.debit.filterWithinDateRange
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.utils.DateHelper.isAfterOrEqual
import br.com.zinid.smartwallet.domain.utils.DateHelper.isBeforeOrEqual
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class DebitPaymentMethod(
    override val id: Long = 0L,
    override val type: PaymentType,
    override val financialAccount: FinancialAccount,
    override val expenses: List<DebitExpense> = listOf()
) : PaymentMethod {

    override fun getRemainingSpendableValue(): BigDecimal =
        financialAccount.getRemainingSpendableValue()

    override fun canPurchase(expense: Expense): Boolean =
        financialAccount.hasBalance(expense.price)

    override fun getMonthlyExpenses(yearMonth: YearMonth?): List<Expense> {
        val date = if (yearMonth == null) LocalDate.now().withDayOfMonth(1)
        else LocalDate.of(yearMonth.year, yearMonth.month, 1)

        return expenses.filter {
            it.date.isAfterOrEqual(date) && it.date.isBeforeOrEqual(date.withDayOfMonth(date.lengthOfMonth()))
        }
    }

    override fun getMonthlyExpensesValue(yearMonth: YearMonth?): BigDecimal =
        getMonthlyExpenses(yearMonth).sumOf { it.price }

    override fun getExpensesWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<DebitExpense> =
        expenses.filterWithinDateRange(startDate, endDate)

    override fun getExpensesValueWithinDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal =
        getExpensesWithinDateRange(startDate, endDate).sumOf { it.price }

    fun processExpense(expense: Expense) =
        financialAccount.deductFromBalance(expense.price)

    companion object {
        fun createBlank() = DebitPaymentMethod(
            id = 0L,
            PaymentType.BLANK,
            financialAccount = FinancialAccount.createBlank()
        )

        fun createBlankFromId(id: Long) = DebitPaymentMethod(
            id = id,
            PaymentType.BLANK,
            financialAccount = FinancialAccount.createBlank()
        )
    }
}
