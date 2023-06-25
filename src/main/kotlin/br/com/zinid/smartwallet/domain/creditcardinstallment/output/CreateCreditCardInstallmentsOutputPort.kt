package br.com.zinid.smartwallet.domain.creditcardinstallment.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense

interface CreateCreditCardInstallmentsOutputPort {

    fun createFromExpense(creditExpense: CreditExpense): CreditCardInstallments?
}
