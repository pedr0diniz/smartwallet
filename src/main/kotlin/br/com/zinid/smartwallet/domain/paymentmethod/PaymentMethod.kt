package br.com.zinid.smartwallet.domain.paymentmethod

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import java.math.BigDecimal
import java.time.LocalDate

interface PaymentMethod {
    val id: Long
    val type: PaymentType
    val financialAccount: FinancialAccount
    val expenses: List<Expense>

    fun getRemainingSpendableValue(): BigDecimal

    fun canPurchase(expense: Expense): Boolean

    fun getMonthlyExpenses(): List<Expense>

    fun getMonthlyExpensesValue(): BigDecimal

    fun getExpensesWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<Expense>

    fun getExpensesValueWithinDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal
}
