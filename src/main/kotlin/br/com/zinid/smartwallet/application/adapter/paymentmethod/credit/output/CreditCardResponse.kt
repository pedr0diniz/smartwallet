package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseResponse
import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodResponse
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCardResponse(
    val id: Long? = null,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    val invoiceDueDayOfMonth: Int,
    val previousInvoiceClosingDate: LocalDate,
    val previousInvoiceDueDate: LocalDate,
    val currentInvoiceClosingDate: LocalDate,
    val currentInvoiceDueDate: LocalDate,
    val expenses: List<CreditExpenseResponse>
) : PaymentMethodResponse {

    companion object {
        fun fromDomain(creditCard: CreditCard) = CreditCardResponse(
            id = creditCard.id,
            last4Digits = creditCard.last4Digits,
            expirationDate = creditCard.expirationDate,
            cardLimit = creditCard.cardLimit,
            invoiceDueDayOfMonth = creditCard.invoiceDueDayOfMonth,
            previousInvoiceClosingDate = creditCard.previousInvoiceClosingDate,
            previousInvoiceDueDate = creditCard.previousInvoiceDueDate,
            currentInvoiceClosingDate = creditCard.currentInvoiceClosingDate,
            currentInvoiceDueDate = creditCard.currentInvoiceDueDate,
            expenses = creditCard.expenses?.map { CreditExpenseResponse.fromDomain(it) } ?: emptyList()
        )
    }
}
