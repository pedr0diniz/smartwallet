package br.com.zinid.smartwallet.domain.paymentmethod.debit.output

import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

interface CreateDebitPaymentMethodOutputPort {

    fun create(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethod
}
