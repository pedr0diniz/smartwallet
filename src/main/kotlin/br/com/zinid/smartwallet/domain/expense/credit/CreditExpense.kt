package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
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
    val numberOfInstallments: Int? = null
) : Expense {

    // TODO - try to make this have a private set
    var creditCardInstallments: CreditCardInstallments? = null

    override fun getPaymentType(): PaymentType = paymentMethod.type
    override fun process(): Boolean {
        buildInstallments()
        return creditCardInstallments != null
    }

    fun hasInstallments() = (creditCardInstallments != null)

    private fun buildInstallments() {
        if (numberOfInstallments != null) {
            creditCardInstallments = CreditCardInstallments.createFromExpenseAndCreditCard(
                expense = this,
                creditCard = paymentMethod
            )
        }
    }

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
                monthlySubscription = monthlySubscription
            )
        }

        return null
    }

    private fun getCurrentInstallment(): CreditCardInstallment? =
        creditCardInstallments?.getInstallmentsByPeriod(
            paymentMethod.previousInvoiceDueDate,
            paymentMethod.currentInvoiceDueDate
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
}

typealias CreditExpenses = List<CreditExpense>

fun CreditExpenses.filterWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<CreditExpense> =
    this.filter {
        it.wasPurchasedWithinDateRange(startDate, endDate) &&
            it.creditCardInstallments == null
    }

fun CreditExpenses.getOngoingInstallments(previousInvoiceClosingDate: LocalDate): List<CreditCardInstallment> {
    val installments = mutableListOf<CreditCardInstallment>()
    this.forEach { expense ->
        expense.creditCardInstallments?.getOngoingInstallments(previousInvoiceClosingDate)
            ?.let {
                installments.addAll(it)
            }
    }

    return installments
}

fun CreditExpenses.getOngoingInstallmentsValue(previousInvoiceClosingDate: LocalDate): BigDecimal =
    this.getOngoingInstallments(previousInvoiceClosingDate).sumOf { it.installmentValue }

fun CreditExpenses.getCurrentMonthInstallmentsAsExpenses(): List<CreditExpense> {
    val expenses = mutableListOf<CreditExpense>()
    this.forEach { expense ->
        expense.getCurrentMonthInstallmentAsExpense()
            ?.let { expenses.add(it) }
    }

    return expenses
}
