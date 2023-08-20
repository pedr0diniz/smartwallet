package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import br.com.zinid.smartwallet.application.adapter.expense.ExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.ExpenseRepository
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.expense.debit.output.CreateDebitExpenseOutputPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CreateDebitExpenseAdapter(
    private val debitExpenseRepository: DebitExpenseRepository,
    private val expenseRepository: ExpenseRepository
) : CreateDebitExpenseOutputPort {

    @Transactional
    override fun create(debitExpense: DebitExpense): DebitExpense? {
        val debitExpenseEntity = DebitExpenseEntity.fromDomain(debitExpense)

        expenseRepository.save(ExpenseEntity.from(debitExpenseEntity))

        return debitExpenseRepository.save(debitExpenseEntity).toDomain()
    }
}
