package br.com.zinid.smartwallet.application.adapter.expense

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.FindExpenseOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindExpenseAdapter(
    private val expenseRepository: ExpenseRepository
) : FindExpenseOutputPort {
    override fun findById(id: Long): Expense? =
        expenseRepository.findByIdOrNull(id)?.toDomain()

    override fun findByCreditExpenseId(creditExpenseId: Long): CreditExpense? =
        expenseRepository.findByCreditExpenseId(creditExpenseId)?.creditExpense?.toDomain()

    override fun findByDebitExpenseId(debitExpenseId: Long): DebitExpense? =
        expenseRepository.findByDebitExpenseId(debitExpenseId)?.debitExpense?.toDomain()
}
