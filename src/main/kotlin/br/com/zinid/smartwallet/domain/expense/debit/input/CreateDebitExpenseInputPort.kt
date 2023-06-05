package br.com.zinid.smartwallet.domain.expense.debit.input

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense

interface CreateDebitExpenseInputPort {

    fun execute(debitExpense: DebitExpense): DebitExpense?
}
