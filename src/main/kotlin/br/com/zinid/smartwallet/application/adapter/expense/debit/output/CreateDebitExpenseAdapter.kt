package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.expense.debit.output.CreateDebitExpenseOutputPort
import org.springframework.stereotype.Service

@Service
class CreateDebitExpenseAdapter(
    private val debitExpenseRepository: DebitExpenseRepository
) : CreateDebitExpenseOutputPort {

    override fun create(debitExpense: DebitExpense): DebitExpense? =
        debitExpenseRepository.save(DebitExpenseEntity.fromDomain(debitExpense)).toDomain()
}
