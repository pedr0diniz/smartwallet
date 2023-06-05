package br.com.zinid.smartwallet.domain.expense.debit.input

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.expense.debit.output.CreateDebitExpenseOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.UpdateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort

class CreateDebitExpenseUseCase(
    private val findDebitPaymentMethodAdapter: FindDebitPaymentMethodOutputPort,
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val updateFinancialAccountAdapter: UpdateFinancialAccountOutputPort,
    private val createDebitExpenseAdapter: CreateDebitExpenseOutputPort
) : CreateDebitExpenseInputPort {

    override fun execute(debitExpense: DebitExpense): DebitExpense? {
        val currentExpense = attachPaymentMethodToExpense(debitExpense) ?: return null

        return processDebitExpense(currentExpense)
    }

    private fun attachPaymentMethodToExpense(debitExpense: DebitExpense): DebitExpense? {
        val possiblePaymentMethod = findDebitPaymentMethodAdapter.findById(debitExpense.paymentMethod.id!!)
            ?: return null

        return debitExpense.copy(paymentMethod = possiblePaymentMethod)
    }

    private fun processDebitExpense(debitExpense: DebitExpense): DebitExpense? {
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(debitExpense.paymentMethod.financialAccount.id!!)
            ?: return null

        return if (possibleFinancialAccount.hasBalance(debitExpense.price)) {
            possibleFinancialAccount.deductFromBalance(debitExpense.price)
            updateFinancialAccountAdapter.update(possibleFinancialAccount)
            createDebitExpenseAdapter.create(debitExpense.copy(paymentMethod = debitExpense.paymentMethod))
        } else {
            println("Insufficient account balance")
            null
        }
    }
}
