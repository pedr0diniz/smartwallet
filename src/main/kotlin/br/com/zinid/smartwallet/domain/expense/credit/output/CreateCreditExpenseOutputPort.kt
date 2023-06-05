package br.com.zinid.smartwallet.domain.expense.credit.output

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense

interface CreateCreditExpenseOutputPort {

    fun create(creditExpense: CreditExpense): CreditExpense?
}
