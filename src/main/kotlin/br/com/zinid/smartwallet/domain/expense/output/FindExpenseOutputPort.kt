package br.com.zinid.smartwallet.domain.expense.output

import br.com.zinid.smartwallet.domain.expense.Expense

interface FindExpenseOutputPort {

    fun findByPaymentMethodId(paymentMethodId: Long): List<Expense>

    fun findById(id: Long): Expense?

}