package br.com.zinid.smartwallet.domain.expense.debit.output

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense

interface FindDebitExpenseOutputPort {

    fun findByDebitPaymentMethodId(debitPaymentMethodId: Long): List<DebitExpense>

    fun findById(id: Long): DebitExpense?
}
