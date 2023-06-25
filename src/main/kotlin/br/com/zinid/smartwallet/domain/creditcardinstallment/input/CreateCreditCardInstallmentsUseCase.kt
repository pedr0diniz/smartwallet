package br.com.zinid.smartwallet.domain.creditcardinstallment.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort

class CreateCreditCardInstallmentsUseCase(
    private val findCreditExpenseAdapter: FindCreditExpenseOutputPort,
    private val createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsOutputPort
) : CreateCreditCardInstallmentsInputPort {

    override fun execute(creditCardInstallments: CreditCardInstallments): CreditCardInstallments? {
        val possibleCreditExpense = findCreditExpenseAdapter.findById(creditCardInstallments.expense.id!!)
            ?: return null

        return createCreditCardInstallmentsAdapter.createFromExpense(possibleCreditExpense)
    }
}
