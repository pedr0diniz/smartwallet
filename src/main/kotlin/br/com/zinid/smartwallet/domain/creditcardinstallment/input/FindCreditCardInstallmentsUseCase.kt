package br.com.zinid.smartwallet.domain.creditcardinstallment.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.FindCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.exception.DomainClasses.CREDIT_EXPENSE
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort

class FindCreditCardInstallmentsUseCase(
    private val findCreditExpenseAdapter: FindCreditExpenseOutputPort,
    private val findCreditCardInstallmentAdapter: FindCreditCardInstallmentsOutputPort
) : FindCreditCardInstallmentsInputPort {
    override fun findByCreditExpenseId(creditExpenseId: Long): CreditCardInstallments? {
        findCreditExpenseAdapter.findById(creditExpenseId)
            ?: throw NotFoundException.buildFrom(CREDIT_EXPENSE, "id", creditExpenseId)

        return findCreditCardInstallmentAdapter.findByCreditExpenseId(creditExpenseId)
    }
}
