package br.com.zinid.smartwallet.domain.expense.credit.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.output.CreateCreditExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort

class CreateCreditExpenseUseCase(
    private val findCreditCardAdapter: FindCreditCardOutputPort,
    private val createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsOutputPort,
    private val createCreditExpenseAdapter: CreateCreditExpenseOutputPort
) : CreateCreditExpenseInputPort {

    override fun execute(creditExpense: CreditExpense): CreditExpense? {
        val currentExpense = attachPaymentMethodToExpense(creditExpense) ?: return null

        return processCreditExpense(currentExpense)
    }

    private fun attachPaymentMethodToExpense(creditExpense: CreditExpense): CreditExpense? {
        val possiblePaymentMethod = findCreditCardAdapter.findById(creditExpense.paymentMethod.id!!)
            ?: return null

        return creditExpense.copy(paymentMethod = possiblePaymentMethod)
    }

    private fun processCreditExpense(creditExpense: CreditExpense): CreditExpense? {
        return if (creditExpense.canBeMade()) {
            val createdExpense = createCreditExpenseAdapter.create(creditExpense) ?: return null

            var createdInstallments: CreditCardInstallments? = null
            if (creditExpense.hasInstallments()) {
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
}
