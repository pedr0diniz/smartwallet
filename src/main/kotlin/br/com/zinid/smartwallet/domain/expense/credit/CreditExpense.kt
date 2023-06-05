package br.com.zinid.smartwallet.domain.expense.credit

import br.com.zinid.smartwallet.domain.acquaintance.Acquaintance
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
