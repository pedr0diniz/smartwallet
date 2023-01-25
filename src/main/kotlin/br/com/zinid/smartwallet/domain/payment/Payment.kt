package br.com.zinid.smartwallet.domain.payment

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod

data class Payment(
    val paymentMethod: PaymentMethod,
    val installments: Int,
    val monthlySubscription: Boolean? = false
)