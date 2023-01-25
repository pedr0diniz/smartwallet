package br.com.zinid.smartwallet.domain.paymentmethod

import br.com.zinid.smartwallet.domain.creditcard.CreditCard

data class PaymentMethod(
    val id: Long,
    val method: PaymentMethods, // pix, débito, crédito, va, vr, etc
    val creditCard: CreditCard?
)
