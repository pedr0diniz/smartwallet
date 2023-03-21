package br.com.zinid.smartwallet.domain.creditcard.output

import br.com.zinid.smartwallet.domain.creditcard.CreditCard

interface FindCreditCardOutputPort {

    fun findById(id: Long): CreditCard?

    fun findByPaymentMethodId(id: Long): CreditCard?
}
