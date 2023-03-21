package br.com.zinid.smartwallet.domain.expense.input

import br.com.zinid.smartwallet.domain.expense.Expense

interface CreateExpenseInputPort {

    fun execute(expense: Expense): Expense?
}
