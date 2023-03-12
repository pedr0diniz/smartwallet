package br.com.zinid.smartwallet.domain.creditcardinstallment.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments

interface CreateCreditCardInstallmentsOutputPort {

    fun create(creditCardInstallments: CreditCardInstallments): Long?

}