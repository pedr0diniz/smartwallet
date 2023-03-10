package br.com.zinid.smartwallet.domain.expense

import br.com.zinid.smartwallet.domain.acquaintance.Acquaintance
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
import java.math.BigDecimal
import java.time.LocalDate

data class Expense(
    val id: Long? = null,
    val content: String? = null,
    val date: LocalDate? = null,
    val price: BigDecimal? = null,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,
    val paymentMethod: PaymentMethod? = null,
    val creditCardInstallments: CreditCardInstallments? = null,
    val expenseFor: Acquaintance? = null
) {
    fun isCreditPurchase() = paymentMethod!!.isCredit()
    fun wasPurchasedWithinDateRange(startDate: LocalDate, endDate: LocalDate): Boolean {
        return (date!!.isEqual(startDate) || date.isAfter(startDate)) && (date.isBefore(endDate))
    }
}