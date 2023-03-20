package br.com.zinid.smartwallet.domain.creditcardinstallment.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments

interface FindCreditCardInstallmentsOutputPort {

    fun findByExpenseId(expenseId: Long): CreditCardInstallments?

}