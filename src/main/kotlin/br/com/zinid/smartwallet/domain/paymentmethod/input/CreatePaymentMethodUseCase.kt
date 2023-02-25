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

    override fun execute(paymentMethod: PaymentMethod): Long? {
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(paymentMethod.financialAccount?.id!!)
            ?: return null

        val paymentMethodId = createPaymentMethodAdapter
            .create(paymentMethod.copy(financialAccount = possibleFinancialAccount))

        if (paymentMethod.creditCard != null) {
            return createCreditCardAdapter
                .create(
                    paymentMethod.creditCard.copy(
                        paymentMethod = PaymentMethod(id = paymentMethodId)
                    )
                )
        }

        return paymentMethodId
    }
}