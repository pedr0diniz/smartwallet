package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import br.com.zinid.smartwallet.application.adapter.expense.ExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.ExpenseRepository
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.output.CreateCreditExpenseOutputPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CreateCreditExpenseAdapter(
    private val creditExpenseRepository: CreditExpenseRepository,
    private val expenseRepository: ExpenseRepository
) : CreateCreditExpenseOutputPort {

    @Transactional
    override fun create(creditExpense: CreditExpense): CreditExpense? {
        val creditExpenseEntity = creditExpenseRepository.save(CreditExpenseEntity.fromDomain(creditExpense))

        expenseRepository.save(ExpenseEntity.from(creditExpenseEntity))

        return creditExpenseEntity.toDomain()
    }
}
