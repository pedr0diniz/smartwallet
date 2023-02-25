package br.com.zinid.smartwallet.domain.creditcard.input

import br.com.zinid.smartwallet.domain.creditcard.CreditCard

interface CreateCreditCardInputPort {

    fun execute(creditCard: CreditCard): Long?

}