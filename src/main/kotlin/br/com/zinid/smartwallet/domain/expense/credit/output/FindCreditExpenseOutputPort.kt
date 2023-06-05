package br.com.zinid.smartwallet.domain.expense.credit.output

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense

interface FindCreditExpenseOutputPort {

    fun findByCreditCardId(creditCardId: Long): List<CreditExpense>

    fun findById(id: Long): CreditExpense?
}
