package br.com.zinid.smartwallet.domain.creditcardinstallment.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.output.FindExpenseOutputPort

class CreateCreditCardInstallmentsUseCase(
    private val findExpenseAdapter: FindExpenseOutputPort,
    private val createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsOutputPort
) : CreateCreditCardInstallmentsInputPort {

    override fun execute(creditCardInstallments: CreditCardInstallments): CreditCardInstallments? {
        val possibleExpense = findExpenseAdapter.findById(creditCardInstallments.expense.id!!) ?: return null

        return createCreditCardInstallmentsAdapter.create(creditCardInstallments.copy(expense = possibleExpense))
    }
}
