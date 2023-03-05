package br.com.zinid.smartwallet.application.adapter.expense.output

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.CreateExpenseOutputPort
import org.springframework.stereotype.Service

@Service
class CreateExpenseAdapter(
    val expenseRepository: ExpenseRepository
) : CreateExpenseOutputPort {
    override fun create(expense: Expense): Long? {
        return expenseRepository.save(ExpenseEntity.fromDomain(expense)).id
    }
}