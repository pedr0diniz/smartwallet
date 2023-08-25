package br.com.zinid.smartwallet.application.adapter.expense

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseResponse
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseResponse
import br.com.zinid.smartwallet.domain.exception.NoExplicitClassException
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

interface ExpenseResponse {
    val id: Long?
    val content: String
    val date: LocalDate
    val price: BigDecimal
    val essential: Boolean?
    val monthlySubscription: Boolean?
    val tag: String?

    companion object {
        fun fromDomain(expense: Expense): ExpenseResponse =
            when (expense) {
                is CreditExpense -> CreditExpenseResponse.fromDomain(expense)
                is DebitExpense -> DebitExpenseResponse.fromDomain(expense)
                else -> throw NoExplicitClassException("Expense must either be credit or debit")
            }
    }
}

data class MonthlyExpensesResponse(
    val yearMonth: YearMonth,
    val monthlyExpensesValue: BigDecimal,
    val expenses: List<ExpenseResponse>
) {
    companion object {
        fun fromExpenseList(expenses: List<ExpenseResponse>) =
            MonthlyExpensesResponse(
                yearMonth = YearMonth.now(),
                monthlyExpensesValue = expenses.sumOf { it.price },
                expenses = expenses.sortedBy { it.date }
            )
    }
}
