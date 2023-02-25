package br.com.zinid.smartwallet.domain.paymentmethod.output

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod

interface FindPaymentMethodOutputPort {

    fun findById(id: Long): PaymentMethod?

}