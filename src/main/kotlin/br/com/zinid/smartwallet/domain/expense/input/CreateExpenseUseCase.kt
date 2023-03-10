package br.com.zinid.smartwallet.domain.expense.input

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.CreateExpenseOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.UpdateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort

class CreateExpenseUseCase(
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort,
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val updateFinancialAccountAdapter: UpdateFinancialAccountOutputPort,
    private val createExpenseAdapter: CreateExpenseOutputPort
) : CreateExpenseInputPort {
    override fun execute(expense: Expense): Long? {
        val possiblePaymentMethod = findPaymentMethodAdapter.findById(expense.paymentMethod.id!!) ?: return null
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(possiblePaymentMethod.financialAccount.id!!)
            ?: return null

        if (!expense.isCreditPurchase() && possibleFinancialAccount.hasBalance(expense.price)) {
            possibleFinancialAccount.deductFromBalance(expense.price)
            updateFinancialAccountAdapter.update(possibleFinancialAccount)
            return createExpenseAdapter.create(expense.copy(paymentMethod = possiblePaymentMethod))
        }

        if (possiblePaymentMethod.hasCreditCardLimit(expense.price)) {
            return createExpenseAdapter.create(expense.copy(paymentMethod = possiblePaymentMethod))
        }

        return null
    }
}