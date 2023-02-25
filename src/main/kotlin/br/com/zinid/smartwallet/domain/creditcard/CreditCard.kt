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
    val installments: List<CreditCardInstallment>? = listOf(),
    val invoiceClosingDate: LocalDate? = null,
    val invoiceDueDate: LocalDate? = null,
    val paymentMethod: PaymentMethod? = null
)
