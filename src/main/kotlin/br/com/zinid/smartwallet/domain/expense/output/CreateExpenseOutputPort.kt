package br.com.zinid.smartwallet.domain.expense.output

import br.com.zinid.smartwallet.domain.expense.Expense

interface CreateExpenseOutputPort {

    fun create(expense: Expense): Expense?

}