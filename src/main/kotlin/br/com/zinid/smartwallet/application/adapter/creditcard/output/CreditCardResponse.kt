package br.com.zinid.smartwallet.application.adapter.creditcard.output

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCardResponse(
    val id: Long? = null,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    val invoiceClosingDayOfMonth: Int,
    val currentInvoiceClosingDate: LocalDate,
    val previousInvoiceClosingDate: LocalDate
) {
    companion object {
        fun fromDomain(creditCard: CreditCard) = CreditCardResponse(
            id = creditCard.id,
            last4Digits = creditCard.last4Digits,
            expirationDate = creditCard.expirationDate,
            cardLimit = creditCard.cardLimit,
            invoiceClosingDayOfMonth = creditCard.invoiceClosingDayOfMonth,
            currentInvoiceClosingDate = creditCard.currentInvoiceClosingDate,
            previousInvoiceClosingDate = creditCard.previousInvoiceClosingDate
        )
    }
}