package br.com.zinid.smartwallet.domain.expense.credit.input

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpenses

interface FindCreditExpenseInputPort {
    fun findByCreditCardId(creditCardId: Long): CreditExpenses
}
