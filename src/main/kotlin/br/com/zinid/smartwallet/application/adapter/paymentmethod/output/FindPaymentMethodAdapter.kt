package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.domain.creditcard.output.FindCreditCardOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindPaymentMethodAdapter(
    val paymentMethodRepository: PaymentMethodRepository,
    val findCreditCardAdapter: FindCreditCardOutputPort
) : FindPaymentMethodOutputPort {
    override fun findById(id: Long): PaymentMethod? {
        val paymentMethod = paymentMethodRepository.findByIdOrNull(id)?.toDomain()
        if (paymentMethod?.method!! == PaymentMethods.CREDIT) {
            return paymentMethod.copy(
                creditCard = findCreditCardAdapter.findByPaymentMethodId(paymentMethod.id!!)
            )
        }

        return paymentMethod
    }
}