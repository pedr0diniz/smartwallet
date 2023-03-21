package br.com.zinid.smartwallet.domain.expense.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.CreateExpenseOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.UpdateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort

class CreateExpenseUseCase(
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort,
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val updateFinancialAccountAdapter: UpdateFinancialAccountOutputPort,
    private val createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsOutputPort,
    private val createExpenseAdapter: CreateExpenseOutputPort
) : CreateExpenseInputPort {
    override fun execute(expense: Expense): Expense? {
        val currentExpense = attachPaymentMethodToExpense(expense) ?: return null

        return processExpense(currentExpense)
    }

    private fun attachPaymentMethodToExpense(expense: Expense): Expense? {
        val possiblePaymentMethod = findPaymentMethodAdapter.findById(expense.paymentMethod.id!!)
            ?: return null

        return expense.copy(paymentMethod = possiblePaymentMethod)
    }

    private fun processExpense(expense: Expense): Expense? {
        if (expense.isCreditPurchase()) {
            return processCreditExpense(expense)
        }

        return processCashExpense(expense)
    }

    private fun processCreditExpense(expense: Expense): Expense? {
        return if (expense.fitsInCreditCardLimit()) {
            val createdExpense = createExpenseAdapter.create(expense) ?: return null

            var createdInstallments: CreditCardInstallments? = null
            if (expense.hasInstallments()) {
                createdInstallments = createCreditCardInstallmentsAdapter.create(
                    createdExpense.buildInstallments()
                )
            }
            createdExpense.copy(creditCardInstallments = createdInstallments)
        } else {
            println("Insufficient card limit")
            null
        }
    }
    private fun processCashExpense(expense: Expense): Expense? {
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(expense.paymentMethod.financialAccount.id!!)
            ?: return null

        return if (possibleFinancialAccount.hasBalance(expense.price)) {
            possibleFinancialAccount.deductFromBalance(expense.price)
            updateFinancialAccountAdapter.update(possibleFinancialAccount)
            createExpenseAdapter.create(expense.copy(paymentMethod = expense.paymentMethod))
        } else {
            println("Insufficient account balance")
            null
        }
    }
}
