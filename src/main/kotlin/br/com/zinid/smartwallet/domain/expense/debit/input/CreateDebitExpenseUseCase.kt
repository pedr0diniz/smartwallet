package br.com.zinid.smartwallet.domain.expense.debit.input

import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
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
        val enrichedExpense = enrichExpense(debitExpense) ?: return null

        return processDebitExpense(enrichedExpense)
    }

    private fun enrichExpense(debitExpense: DebitExpense): DebitExpense? {
        val expenseWithPaymentMethod = attachPaymentMethodToExpense(debitExpense) ?: return null

        return attachFinancialAccountToExpense(expenseWithPaymentMethod)
    }

    private fun attachPaymentMethodToExpense(debitExpense: DebitExpense): DebitExpense? {
        val possiblePaymentMethod = findDebitPaymentMethodAdapter.findById(debitExpense.paymentMethod.id!!)
            ?: return null

        return debitExpense.copy(paymentMethod = possiblePaymentMethod)
    }

    private fun attachFinancialAccountToExpense(debitExpense: DebitExpense): DebitExpense? {
        val possibleFinancialAccount =
            findFinancialAccountAdapter.findById(debitExpense.paymentMethod.financialAccount.id!!)
                ?: return null

        return debitExpense.copy(
            paymentMethod = debitExpense.paymentMethod.copy(
                financialAccount = possibleFinancialAccount
            )
        )
    }

    private fun processDebitExpense(debitExpense: DebitExpense): DebitExpense? {
        return if (debitExpense.canBeMade()) {
            debitExpense.process()
            updateFinancialAccountAdapter.updateByDebitExpense(debitExpense)
            createDebitExpenseAdapter.create(debitExpense)
        } else {
            throw InsufficientBalanceException(
                INSUFFICIENT_BALANCE_MESSAGE.format(
                    debitExpense.price,
                    debitExpense.paymentMethod.getRemainingSpendableValue()
                )
            )
        }
    }

    companion object {
        private const val INSUFFICIENT_BALANCE_MESSAGE = "Cannot deduct [R$ %s] from balance + overdraft of [R$ %s]"
    }
}
