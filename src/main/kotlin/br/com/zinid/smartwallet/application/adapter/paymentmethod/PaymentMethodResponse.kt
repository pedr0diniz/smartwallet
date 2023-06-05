package br.com.zinid.smartwallet.application.adapter.paymentmethod

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardResponse
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodResponse
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

interface PaymentMethodResponse {
    companion object {
        fun fromDomain(paymentMethod: PaymentMethod): PaymentMethodResponse =
            when (paymentMethod) {
                is CreditCard -> CreditCardResponse.fromDomain(paymentMethod)
                is DebitPaymentMethod -> DebitPaymentMethodResponse.fromDomain(paymentMethod)
                else -> throw IllegalStateException("Invalid payment method")
            }
    }
}
