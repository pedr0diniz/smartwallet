package br.com.zinid.smartwallet.domain.paymentmethod.input

import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.output.CreatePaymentMethodOutputPort

class CreatePaymentMethodUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val createPaymentMethodAdapter: CreatePaymentMethodOutputPort
) : CreatePaymentMethodInputPort {

    override fun execute(paymentMethod: PaymentMethod): Long? {
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(paymentMethod.financialAccount?.id!!)
            ?: return null

        return createPaymentMethodAdapter.create(paymentMethod.copy(financialAccount = possibleFinancialAccount))
    }
}