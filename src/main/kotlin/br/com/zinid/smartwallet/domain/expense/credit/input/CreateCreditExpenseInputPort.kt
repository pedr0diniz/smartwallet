package br.com.zinid.smartwallet.domain.expense.credit.input

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense

interface CreateCreditExpenseInputPort {

    fun execute(creditExpense: CreditExpense): CreditExpense?
}
