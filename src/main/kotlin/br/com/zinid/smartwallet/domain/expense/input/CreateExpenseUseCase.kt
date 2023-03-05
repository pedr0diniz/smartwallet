package br.com.zinid.smartwallet.domain.expense.input

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.CreateExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort

class CreateExpenseUseCase(
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort,
    private val createExpenseAdapter: CreateExpenseOutputPort
) : CreateExpenseInputPort {
    override fun execute(expense: Expense): Long? {
        val possiblePaymentMethod = findPaymentMethodAdapter.findById(expense.paymentMethod?.id!!) ?: return null

        return createExpenseAdapter.create(expense.copy(paymentMethod = possiblePaymentMethod))
    }
}