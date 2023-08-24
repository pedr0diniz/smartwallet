package br.com.zinid.smartwallet.domain.expense.debit.input

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpenses

interface FindDebitExpenseInputPort {
    fun findByDebitPaymentMethodId(debitPaymentMethodId: Long): DebitExpenses
}
