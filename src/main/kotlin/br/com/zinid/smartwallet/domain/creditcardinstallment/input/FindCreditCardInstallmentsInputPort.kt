package br.com.zinid.smartwallet.domain.creditcardinstallment.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments

interface FindCreditCardInstallmentsInputPort {
    fun findByCreditExpenseId(creditExpenseId: Long): CreditCardInstallments?
}
