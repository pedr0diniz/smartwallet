package br.com.zinid.smartwallet.domain.paymentmethod.debit.input

import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

interface CreateDebitPaymentMethodInputPort {

    fun execute(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethod?
}
