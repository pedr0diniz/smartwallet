package br.com.zinid.smartwallet.domain.expense

import br.com.zinid.smartwallet.domain.acquaintance.Acquaintance
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

data class Expense(
    val id: Long? = null,
    val content: String,
    val date: LocalDate,
    val price: BigDecimal,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,
    val paymentMethod: PaymentMethod,
    val creditCardInstallments: CreditCardInstallments? = null,
    val expenseFor: Acquaintance? = null
) {
    fun isCreditPurchase() = paymentMethod.isCredit()

    fun wasPurchasedWithinDateRange(startDate: LocalDate, endDate: LocalDate): Boolean =
        (date.isEqual(startDate) || date.isAfter(startDate)) && (date.isBefore(endDate))

    fun hasInstallments() = (creditCardInstallments != null)

    fun fitsInCreditCardLimit() = paymentMethod.hasCreditCardLimit(price)

    fun buildInstallments() = CreditCardInstallments.createFromExpenseAndCreditCard(
        expense = this,
        creditCard = paymentMethod.creditCard!!
    )

    fun getCreditCardInstallmentsByPeriod(startDate: LocalDate, endDate: LocalDate): List<CreditCardInstallment> {
        if (paymentMethod.isCredit()) {
            return creditCardInstallments?.installments?.filter {
                it.dueDate > startDate && it.dueDate <= endDate
            } ?: emptyList()
        }

        return emptyList()
    }

    companion object {
        fun createBlank() = Expense(
            id = 0L,
            content = "",
            date = LocalDate.now(),
            price = BigDecimal.ZERO,
            essential = false,
            monthlySubscription = false,
            paymentMethod = PaymentMethod.createBlank()
        )
    }
}
