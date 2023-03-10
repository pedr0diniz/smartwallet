package br.com.zinid.smartwallet.domain.creditcard

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCard(
    val id: Long? = null,
    val last4Digits: String? = null,
    val expirationDate: LocalDate? = null,
    val cardLimit: BigDecimal? = null,
    val invoiceClosingDayOfMonth: Int? = null,
    val paymentMethod: PaymentMethod? = null
) {
    val currentInvoiceClosingDate: LocalDate = getCurrentClosingDate()
    val previousInvoiceClosingDate: LocalDate = getLastClosingDate()

    fun hasLimit(
        consumedLimit: BigDecimal,
        expenseValue: BigDecimal
    ): Boolean = (cardLimit!! >= consumedLimit.add(expenseValue))

    private fun getLastClosingDate(): LocalDate {
        val today = LocalDate.now()
        if (invoiceClosingDayOfMonth!! > today.dayOfMonth) {
            return today.minusMonths(1).withDayOfMonth(invoiceClosingDayOfMonth)
        }

        return today.withDayOfMonth(invoiceClosingDayOfMonth)
    }

    private fun getCurrentClosingDate(): LocalDate {
        val today = LocalDate.now()
        if (invoiceClosingDayOfMonth!! > today.dayOfMonth) {
            return today.withDayOfMonth(invoiceClosingDayOfMonth)
        }

        return today.plusMonths(1).withDayOfMonth(invoiceClosingDayOfMonth)
    }
}
