package br.com.zinid.smartwallet.domain.expense.debit.input

import br.com.zinid.smartwallet.domain.exception.DomainClasses.DEBIT_PAYMENT_METHOD
import br.com.zinid.smartwallet.domain.exception.DomainClasses.FINANCIAL_ACCOUNT
import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
import br.com.zinid.smartwallet.domain.exception.NotFoundException
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

    override fun execute(debitExpense: DebitExpense): DebitExpense {
        val enrichedExpense = enrichExpense(debitExpense)

        return processDebitExpense(enrichedExpense)
    }

    private fun enrichExpense(debitExpense: DebitExpense): DebitExpense {
        val expenseWithPaymentMethod = attachPaymentMethodToExpense(debitExpense)

        return attachFinancialAccountToExpense(expenseWithPaymentMethod)
    }

    private fun attachPaymentMethodToExpense(debitExpense: DebitExpense): DebitExpense {
        val paymentMethodId = debitExpense.paymentMethod.id!!
        val possiblePaymentMethod = findDebitPaymentMethodAdapter.findById(paymentMethodId)
            ?: throw NotFoundException.buildFrom(DEBIT_PAYMENT_METHOD, "id", paymentMethodId)

        return debitExpense.copy(paymentMethod = possiblePaymentMethod)
    }

    private fun attachFinancialAccountToExpense(debitExpense: DebitExpense): DebitExpense {
        val financialAccountId = debitExpense.paymentMethod.financialAccount.id!!
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(financialAccountId)
            ?: throw NotFoundException.buildFrom(FINANCIAL_ACCOUNT, "id", financialAccountId)

        return debitExpense.copy(
            paymentMethod = debitExpense.paymentMethod.copy(
                financialAccount = possibleFinancialAccount
            )
        )
    }

    private fun processDebitExpense(debitExpense: DebitExpense): DebitExpense {
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
