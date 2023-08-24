package br.com.zinid.smartwallet.domain.expense.credit.input

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpenses
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort

class FindCreditExpenseUseCase(
    private val findCreditExpenseAdapter: FindCreditExpenseOutputPort
) : FindCreditExpenseInputPort {
    override fun findByCreditCardId(creditCardId: Long): CreditExpenses =
        findCreditExpenseAdapter.findByCreditCardId(creditCardId)
}
