package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.utils.DateHelper
import br.com.zinid.smartwallet.domain.utils.DateHelper.isBeforeOrEqual
import java.math.BigDecimal
import java.time.LocalDate

data class CreditExpense(
    override val id: Long = 0L,
    override val content: String,
    override val date: LocalDate,
    override val price: BigDecimal,
    override val essential: Boolean? = false,
    override val monthlySubscription: Boolean? = false,
    override val paymentMethod: CreditCard,
    override val tag: String? = null,
    val numberOfInstallments: Int? = null,
    var dueDate: LocalDate? = null
) : Expense {

    var creditCardInstallments: CreditCardInstallments? = null

    override fun getPaymentType(): PaymentType = paymentMethod.type
    override fun process() {
        val expenseDueDate = getExpenseDueDate()

        if (numberOfInstallments != null) {
            buildInstallments(expenseDueDate)
        } else {
            dueDate = expenseDueDate
        }
    }

    fun hasInstallments() = (creditCardInstallments != null)

    private fun buildInstallments(firstInstallmentDueDate: LocalDate) {
        creditCardInstallments = CreditCardInstallments.createFromExpenseAndCreditCard(
            expense = this,
            creditCard = paymentMethod,
            firstInstallmentDueDate = firstInstallmentDueDate
        )
    }

    private fun getExpenseDueDate(): LocalDate {
        val currentDueDate = DateHelper.getCurrentDueDate(paymentMethod.invoiceDueDayOfMonth, date)

        return when (
            date.isBeforeOrEqual(
                currentDueDate.minusDays(paymentMethod.delayBetweenClosingAndDueDays.toLong())
            )
        ) {
            true -> currentDueDate
            false -> currentDueDate.plusMonths(1)
        }
    }

    fun getCreditCardInstallmentsByPeriod(startDate: LocalDate, endDate: LocalDate): List<CreditCardInstallment> {
        return creditCardInstallments?.installments?.filter {
            it.dueDate > startDate && it.dueDate <= endDate
        } ?: emptyList()
    }

    fun getInstallmentWithinDateRangeAsExpense(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ): CreditExpense? {

        val possibleInstallment = getInstallmentWithinDateRange(
            startDate ?: paymentMethod.previousInvoiceClosingDate,
            endDate ?: paymentMethod.currentInvoiceClosingDate
        )

        val installmentsList = creditCardInstallments?.installments
        val installmentMonth = installmentsList?.indexOf(possibleInstallment)?.plus(1)
        val installmentDetailMessage = if (installmentMonth != null) "$installmentMonth / ${installmentsList.size}"
        else ""

        if (possibleInstallment != null) {
            return CreditExpense(
                content = "Parcela $installmentDetailMessage de $content",
                date = date,
                price = possibleInstallment.installmentValue,
                paymentMethod = paymentMethod,
                essential = essential,
                monthlySubscription = monthlySubscription,
                tag = tag
            )
        }

        return null
    }

    private fun getInstallmentWithinDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): CreditCardInstallment? = creditCardInstallments?.getInstallmentsByPeriod(startDate, endDate)?.firstOrNull()

    companion object {
        fun createBlank() = CreditExpense(
            id = 0L,
            content = "",
            date = LocalDate.now(),
            price = BigDecimal.ZERO,
            essential = false,
            monthlySubscription = false,
            tag = "",
            paymentMethod = CreditCard.createBlank()
        )
    }
}

typealias CreditExpenses = List<CreditExpense>

fun CreditExpenses.filterWithinDateRange(startDate: LocalDate, endDate: LocalDate): List<CreditExpense> =
    this.filter {
        it.wasPurchasedWithinDateRange(startDate, endDate) && it.creditCardInstallments == null
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

fun CreditExpenses.getInstallmentsWithinDateRangeAsExpenses(
    startDate: LocalDate,
    endDate: LocalDate
): List<CreditExpense> {
    val expenses = mutableListOf<CreditExpense>()
    this.forEach { expense ->
        expense.getInstallmentWithinDateRangeAsExpense(startDate, endDate)
            ?.let { expenses.add(it) }
    }

    return expenses
}

fun CreditExpenses.filterByTag(tag: String): List<CreditExpense> = this.filter { it.tag == tag }
