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
    override fun execute(expense: Expense): Long? {
        val possiblePaymentMethod = findPaymentMethodAdapter.findById(expense.paymentMethod.id!!) ?: return null
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(possiblePaymentMethod.financialAccount.id!!)
            ?: return null

        val currentExpense = expense.copy(paymentMethod = possiblePaymentMethod)
        if (currentExpense.isCreditPurchase()) {
            if (possiblePaymentMethod.hasCreditCardLimit(currentExpense.price)) {
                val expenseId = createExpenseAdapter.create(currentExpense)
                if (currentExpense.hasInstallments()) {
                    val installments = CreditCardInstallments.createFromExpenseAndCreditCard(
                        currentExpense.copy(id = expenseId),
                        possiblePaymentMethod.creditCard!!
                    )
                    createCreditCardInstallmentsAdapter.create(installments)
                }
                return expenseId
            }
            println("Insufficient card limit")
            return null
        }

        if (possibleFinancialAccount.hasBalance(expense.price)) {
            possibleFinancialAccount.deductFromBalance(expense.price)
            updateFinancialAccountAdapter.update(possibleFinancialAccount)
            return createExpenseAdapter.create(expense.copy(paymentMethod = possiblePaymentMethod))
        }

        println("Insufficient account balance")
        return null
    }
}