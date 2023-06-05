package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.output.CreateCreditExpenseOutputPort
import org.springframework.stereotype.Service

@Service
class CreateCreditExpenseAdapter(
    private val creditExpenseRepository: CreditExpenseRepository
) : CreateCreditExpenseOutputPort {

    override fun create(creditExpense: CreditExpense): CreditExpense? =
        creditExpenseRepository.save(CreditExpenseEntity.fromDomain(creditExpense)).toDomain()
}
