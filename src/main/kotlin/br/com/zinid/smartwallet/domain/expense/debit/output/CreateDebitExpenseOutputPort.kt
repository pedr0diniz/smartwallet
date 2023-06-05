package br.com.zinid.smartwallet.domain.expense.debit.output

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense

interface CreateDebitExpenseOutputPort {

    fun create(debitExpense: DebitExpense): DebitExpense?
}
