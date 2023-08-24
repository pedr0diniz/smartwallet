package br.com.zinid.smartwallet.domain.expense.debit.input

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpenses
import br.com.zinid.smartwallet.domain.expense.debit.output.FindDebitExpenseOutputPort

class FindDebitExpenseUseCase(
    private val findDebitExpenseAdapter: FindDebitExpenseOutputPort
) : FindDebitExpenseInputPort {
    override fun findByDebitPaymentMethodId(debitPaymentMethodId: Long): DebitExpenses =
        findDebitExpenseAdapter.findByDebitPaymentMethodId(debitPaymentMethodId)
}
