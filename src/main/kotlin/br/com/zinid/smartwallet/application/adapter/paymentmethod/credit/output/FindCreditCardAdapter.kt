package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output

import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindCreditCardAdapter(
    private val creditCardRepository: CreditCardRepository,
    private val findCreditExpenseAdapter: FindCreditExpenseOutputPort
) : FindCreditCardOutputPort {
    override fun findById(creditCardId: Long): CreditCard? {
        val possibleCreditCard = creditCardRepository.findByIdOrNull(creditCardId)

        return possibleCreditCard?.toDomain(
            creditExpenses = getCreditExpenses(possibleCreditCard.id!!)
        )
    }

    override fun findByFinancialAccountId(financialAccountId: Long): List<CreditCard> =
        creditCardRepository.findByFinancialAccountId(financialAccountId)
            .map { it.toDomain(creditExpenses = getCreditExpenses(it.id!!)) }

    private fun getCreditExpenses(id: Long) = findCreditExpenseAdapter.findByCreditCardId(id)
}
