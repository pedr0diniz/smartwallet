package br.com.zinid.smartwallet.domain.paymentmethod.debit.input

import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort

class FindDebitPaymentMethodUseCase(
    private val findDebitPaymentMethodAdapter: FindDebitPaymentMethodOutputPort
) : FindDebitPaymentMethodInputPort {
    override fun findByFinancialAccountId(financialAccountId: Long): List<DebitPaymentMethod> =
        findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)
}
