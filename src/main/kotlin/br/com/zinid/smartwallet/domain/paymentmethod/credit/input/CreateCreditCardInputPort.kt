package br.com.zinid.smartwallet.domain.paymentmethod.credit.input

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard

interface CreateCreditCardInputPort {

    fun execute(creditCard: CreditCard): CreditCard?
}
