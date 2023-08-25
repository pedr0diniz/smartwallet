package br.com.zinid.smartwallet.domain.expense.debit

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

data class DebitExpense(
    override val id: Long? = null,
    override val content: String,
    override val date: LocalDate,
    override val price: BigDecimal,
    override val essential: Boolean? = false,
    override val monthlySubscription: Boolean? = false,
    override val paymentMethod: DebitPaymentMethod,
    override val tag: String? = null
) : Expense {
    override fun getPaymentType(): PaymentType = paymentMethod.type

    override fun process() = paymentMethod.processExpense(this)

    fun getFinancialAccount(): FinancialAccount = paymentMethod.financialAccount
}

typealias DebitExpenses = List<DebitExpense>

fun DebitExpenses.filterWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<DebitExpense> =
    this.filter { it.wasPurchasedWithinDateRange(startDate, endDate) }

fun DebitExpenses.filterByTag(tag: String): List<DebitExpense> = this.filter { it.tag == tag }
