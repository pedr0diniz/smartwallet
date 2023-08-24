package br.com.zinid.smartwallet.domain.paymentmethod

class FindPaymentMethodUseCase(
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort
) : FindPaymentMethodInputPort {
    override fun findByFinancialAccountId(financialAccountId: Long): List<PaymentMethod> =
        findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)
}
