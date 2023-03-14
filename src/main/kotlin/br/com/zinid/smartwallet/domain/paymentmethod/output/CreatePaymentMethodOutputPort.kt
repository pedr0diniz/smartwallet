package br.com.zinid.smartwallet.domain.paymentmethod.output

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod

interface CreatePaymentMethodOutputPort {

    fun create(paymentMethod: PaymentMethod): PaymentMethod?

}