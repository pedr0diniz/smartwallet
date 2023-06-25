package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.output.FindCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindCreditExpenseAdapter(
    private val creditExpenseRepository: CreditExpenseRepository,
    private val findCreditCardInstallmentsAdapter: FindCreditCardInstallmentsOutputPort
) : FindCreditExpenseOutputPort {

    override fun findById(id: Long): CreditExpense? {
        val possibleCreditExpense = creditExpenseRepository.findByIdOrNull(id)?.toDomain()

        return possibleCreditExpense?.apply {
            creditCardInstallments = getCreditCardInstallments(id)
        }
    }

    override fun findByCreditCardId(creditCardId: Long): List<CreditExpense> =
        creditExpenseRepository.findByPaymentMethodId(creditCardId).map {
            it.toDomain().apply {
                creditCardInstallments = getCreditCardInstallments(it.id!!)
            }
        }

    private fun getCreditCardInstallments(id: Long) = findCreditCardInstallmentsAdapter.findByCreditExpenseId(id)
}
