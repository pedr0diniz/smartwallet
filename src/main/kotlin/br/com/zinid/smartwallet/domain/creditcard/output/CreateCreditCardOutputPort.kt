package br.com.zinid.smartwallet.domain.creditcard.output

import br.com.zinid.smartwallet.domain.creditcard.CreditCard

interface CreateCreditCardOutputPort {

    fun create(creditCard: CreditCard): CreditCard?
}
