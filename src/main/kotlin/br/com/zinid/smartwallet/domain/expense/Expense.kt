package br.com.zinid.smartwallet.domain.expense

import br.com.zinid.smartwallet.domain.acquaintance.Acquaintance
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
    fun wasPurchasedWithinDateRange(startDate: LocalDate, endDate: LocalDate): Boolean {
        return (date.isEqual(startDate) || date.isAfter(startDate)) && (date.isBefore(endDate))
    }
}