package br.com.zinid.smartwallet.application.adapter.expense.output

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.FindExpenseOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindExpenseAdapter(
    val expenseRepository: ExpenseRepository
) : FindExpenseOutputPort {
    override fun findByPaymentMethodId(paymentMethodId: Long): List<Expense> {
        return expenseRepository.findByPaymentMethodId(paymentMethodId).map { it.toDomain() }
    }

    override fun findById(id: Long): Expense? {
        return expenseRepository.findByIdOrNull(id)?.toDomain()
    }
}