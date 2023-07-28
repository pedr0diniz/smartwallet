package br.com.zinid.smartwallet.domain.expense

interface FindExpenseOutputPort {

    fun findById(id: Long): Expense?

    fun findByCreditExpenseId(creditExpenseId: Long): Expense?

    fun findByDebitExpenseId(debitExpenseId: Long): Expense?
}
