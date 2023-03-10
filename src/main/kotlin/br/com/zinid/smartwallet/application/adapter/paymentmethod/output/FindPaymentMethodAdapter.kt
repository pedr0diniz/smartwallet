package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.domain.creditcard.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.domain.expense.output.FindExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
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
        val possibleExpenses = findExpenseAdapter.findByPaymentMethodId(id)
        val possibleCreditCard = findCreditCardAdapter.findByPaymentMethodId(id)

        return possiblePaymentMethod?.copy(
            expenses = possibleExpenses,
            creditCard = possibleCreditCard
        )
    }
}