package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.acquaintance.Acquaintance
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.utils.DateHelper.isAfterOrEqual
import java.math.BigDecimal
import java.time.LocalDate

data class CreditExpense(
    override val id: Long? = null,
    override val content: String,
    override val date: LocalDate,
    override val price: BigDecimal,
    override val essential: Boolean? = false,
    override val monthlySubscription: Boolean? = false,
    override val paymentMethod: CreditCard,
    val numberOfInstallments: Int? = null,
    val creditCardInstallments: CreditCardInstallments? = null,
    val expenseFor: Acquaintance? = null
) : Expense {
    fun hasInstallments() = (creditCardInstallments != null)

    fun buildInstallments(): CreditCardInstallments =
        CreditCardInstallments.createFromExpenseAndCreditCard(
            expense = this,
            creditCard = paymentMethod
        )

    fun getCreditCardInstallmentsByPeriod(startDate: LocalDate, endDate: LocalDate): List<CreditCardInstallment> {
        return creditCardInstallments?.installments?.filter {
            it.dueDate > startDate && it.dueDate <= endDate
        } ?: emptyList()
    }

    fun getCurrentMonthInstallmentAsExpense(): CreditExpense? {
        val possibleInstallment = getCurrentInstallment()

        if (possibleInstallment != null) {
            return CreditExpense(
                content = "Parcela de $content",
                date = date,
                price = possibleInstallment.installmentValue,
                paymentMethod = paymentMethod,
                essential = essential,
                monthlySubscription = monthlySubscription,
                expenseFor = expenseFor
            )
        }

        return null
    }

    private fun getCurrentInstallment(): CreditCardInstallment? =
        creditCardInstallments?.getInstallmentsByPeriod(
            paymentMethod.currentInvoiceClosingDate,
            paymentMethod.currentInvoiceClosingDate.plusMonths(1)
        )?.firstOrNull()

    companion object {
        fun createBlank() = CreditExpense(
            id = 0L,
            content = "",
            date = LocalDate.now(),
            price = BigDecimal.ZERO,
            essential = false,
            monthlySubscription = false,
            paymentMethod = CreditCard.createBlank()
        )
    }

    override fun getPaymentType(): PaymentType = paymentMethod.type
}

fun List<CreditExpense>.filterWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<CreditExpense> {
    if (startDate.isAfterOrEqual(endDate)) throw IllegalStateException("Invalid date range")

    return this.filter {
        it.wasPurchasedWithinDateRange(startDate, endDate) &&
            it.creditCardInstallments == null
    }
}

fun List<CreditExpense>.getOngoingInstallments(previousInvoiceClosingDate: LocalDate): List<CreditCardInstallment> {
    val installments = mutableListOf<CreditCardInstallment>()
    this.forEach { expense ->
        expense.creditCardInstallments?.getOngoingInstallments(previousInvoiceClosingDate)
            ?.let {
                installments.addAll(it)
            }
    }

    return installments
}

fun List<CreditExpense>.getOngoingInstallmentsValue(previousInvoiceClosingDate: LocalDate): BigDecimal =
    this.getOngoingInstallments(previousInvoiceClosingDate).sumOf { it.installmentValue }

fun List<CreditExpense>.getOngoingInstallmentsAsExpenses(): List<CreditExpense> {
    val expenses = mutableListOf<CreditExpense>()
    this.forEach { expense ->
        expense.getCurrentMonthInstallmentAsExpense()
            ?.let { expenses.add(it) }
    }

    return expenses
}
