package br.com.zinid.smartwallet.domain.creditcardinstallment.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments

interface CreateCreditCardInstallmentsInputPort {

    fun execute(creditCardInstallments: CreditCardInstallments): CreditCardInstallments?

}