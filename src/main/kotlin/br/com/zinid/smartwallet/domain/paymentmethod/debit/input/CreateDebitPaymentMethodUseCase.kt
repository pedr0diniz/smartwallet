package br.com.zinid.smartwallet.domain.paymentmethod.debit.input

import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.CreateDebitPaymentMethodOutputPort

class CreateDebitPaymentMethodUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val createDebitPaymentMethodAdapter: CreateDebitPaymentMethodOutputPort
) : CreateDebitPaymentMethodInputPort {

    override fun execute(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethod? {

        val possibleFinancialAccount = findFinancialAccountAdapter.findById(debitPaymentMethod.financialAccount.id!!)
            ?: return null

        return createDebitPaymentMethodAdapter.create(debitPaymentMethod.copy(financialAccount = possibleFinancialAccount))
    }
}
