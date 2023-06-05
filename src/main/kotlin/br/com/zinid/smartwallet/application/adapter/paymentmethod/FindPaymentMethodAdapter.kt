package br.com.zinid.smartwallet.application.adapter.paymentmethod

import br.com.zinid.smartwallet.domain.paymentmethod.FindPaymentMethodOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindPaymentMethodAdapter(
    private val findCreditCardAdapter: FindCreditCardOutputPort,
    private val findDebitPaymentMethodAdapter: FindDebitPaymentMethodOutputPort,
    private val paymentMethodRepository: PaymentMethodRepository
) : FindPaymentMethodOutputPort {
    override fun findById(id: Long): PaymentMethod? =
        paymentMethodRepository.findByIdOrNull(id)?.toDomain()

    override fun findByCreditCardId(creditCardId: Long): PaymentMethod? =
        findCreditCardAdapter.findById(creditCardId)

    override fun findByDebitPaymentMethodId(debitPaymentMethodId: Long): PaymentMethod? =
        findDebitPaymentMethodAdapter.findById(debitPaymentMethodId)

    override fun findByFinancialAccountId(financialAccountId: Long): List<PaymentMethod> {
        val paymentMethods = mutableListOf<PaymentMethod>()
        paymentMethods.addAll(findCreditCardAdapter.findByFinancialAccountId(financialAccountId))
        paymentMethods.addAll(findDebitPaymentMethodAdapter.findByFinancialAccountId(financialAccountId))

        return paymentMethods
    }
}
