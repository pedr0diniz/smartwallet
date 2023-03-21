package br.com.zinid.smartwallet.domain.paymentmethod.input

import br.com.zinid.smartwallet.domain.creditcard.output.CreateCreditCardOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.output.CreatePaymentMethodOutputPort

class CreatePaymentMethodUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val createPaymentMethodAdapter: CreatePaymentMethodOutputPort,
    private val createCreditCardAdapter: CreateCreditCardOutputPort,
) : CreatePaymentMethodInputPort {

    override fun execute(paymentMethod: PaymentMethod): PaymentMethod? {
        val createdPaymentMethod = createPaymentMethod(paymentMethod) ?: return null

        val createdCreditCard = if (paymentMethod.creditCard != null) {
            createCreditCardAdapter
                .create(
                    paymentMethod.creditCard!!.copy(
                        paymentMethod = createdPaymentMethod
                    )
                )
        } else {
            null
        }

        return createdPaymentMethod.copy(creditCard = createdCreditCard)
    }

    private fun createPaymentMethod(paymentMethod: PaymentMethod): PaymentMethod? {
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(paymentMethod.financialAccount.id!!)
            ?: return null

        return createPaymentMethodAdapter.create(paymentMethod.copy(financialAccount = possibleFinancialAccount))
    }
}
