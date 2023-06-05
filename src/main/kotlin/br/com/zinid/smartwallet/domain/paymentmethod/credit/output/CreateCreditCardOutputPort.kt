package br.com.zinid.smartwallet.domain.paymentmethod.credit.output

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard

interface CreateCreditCardOutputPort {

    fun create(creditCard: CreditCard): CreditCard?
}
