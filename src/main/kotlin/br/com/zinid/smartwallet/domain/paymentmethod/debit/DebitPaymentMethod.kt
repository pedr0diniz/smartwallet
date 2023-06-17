package br.com.zinid.smartwallet.domain.paymentmethod.debit

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.expense.debit.filterWithinDateRange
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import java.math.BigDecimal
import java.time.LocalDate

data class DebitPaymentMethod(
    override val id: Long? = null,
    override val type: PaymentType,
    override val financialAccount: FinancialAccount,
    override val expenses: List<DebitExpense>? = listOf()
) : PaymentMethod {

    override fun getRemainingSpendableValue(): BigDecimal =
        financialAccount.balance.add(financialAccount.overdraft)

    override fun canPurchase(expenseValue: BigDecimal): Boolean =
        getRemainingSpendableValue().minus(expenseValue) >= BigDecimal.ZERO

    override fun getMonthlyExpenses(): List<DebitExpense> =
        expenses?.filter {
            it.date.isAfter(LocalDate.now().withDayOfMonth(1))
        } ?: emptyList()

    override fun getMonthlyExpensesValue(): BigDecimal =
        getMonthlyExpenses().sumOf { it.price }

    override fun getExpensesWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<DebitExpense> =
        expenses?.filterWithinDateRange(startDate, endDate) ?: emptyList()

    override fun getExpensesValueWithinDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal =
        getExpensesWithinDateRange(startDate, endDate).sumOf { it.price }

    companion object {
        fun createBlank() = DebitPaymentMethod(
            id = 0L,
            PaymentType.BLANK,
            financialAccount = FinancialAccount.createBlank()
        )
    }
}
