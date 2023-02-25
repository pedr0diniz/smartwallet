package br.com.zinid.smartwallet.domain.paymentmethod.input

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod

interface CreatePaymentMethodInputPort {

    fun execute(paymentMethod: PaymentMethod): Long?

}