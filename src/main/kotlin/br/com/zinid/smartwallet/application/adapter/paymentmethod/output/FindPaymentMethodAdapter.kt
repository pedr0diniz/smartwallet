package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.domain.creditcard.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.domain.expense.output.FindExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindPaymentMethodAdapter(
    val paymentMethodRepository: PaymentMethodRepository,
    val findCreditCardAdapter: FindCreditCardOutputPort,
    val findExpenseAdapter: FindExpenseOutputPort
) : FindPaymentMethodOutputPort {
    override fun findById(id: Long): PaymentMethod? {
        val possiblePaymentMethod = paymentMethodRepository.findByIdOrNull(id)?.toDomain()

        return possiblePaymentMethod?.copy(
            expenses = getExpenses(id),
            creditCard = getCreditCard(id)
        )
    }

    override fun findByFinancialAccountId(financialAccountId: Long): List<PaymentMethod> {
        return paymentMethodRepository.findByFinancialAccountId(financialAccountId).map {
            it.toDomain().copy(
                expenses = getExpenses(it.id!!),
                creditCard = getCreditCard(it.id)
            )
        }
    }

    private fun getExpenses(id: Long) = findExpenseAdapter.findByPaymentMethodId(id)

    private fun getCreditCard(id: Long) = findCreditCardAdapter.findByPaymentMethodId(id)
}
