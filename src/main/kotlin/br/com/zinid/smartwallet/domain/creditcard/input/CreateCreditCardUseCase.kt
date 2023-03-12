package br.com.zinid.smartwallet.domain.creditcard.input

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.creditcard.output.CreateCreditCardOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort

class CreateCreditCardUseCase(
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort,
    private val createCreditCardAdapter: CreateCreditCardOutputPort
) : CreateCreditCardInputPort {

    override fun execute(creditCard: CreditCard): Long? {
        val possiblePaymentMethod = findPaymentMethodAdapter.findById(creditCard.paymentMethod.id!!) ?: return null

        return createCreditCardAdapter.create(creditCard.copy(paymentMethod = possiblePaymentMethod))
    }
}