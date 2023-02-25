package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindPaymentMethodAdapter(
    val paymentMethodRepository: PaymentMethodRepository
) : FindPaymentMethodOutputPort {
    override fun findById(id: Long): PaymentMethod? {
        return paymentMethodRepository.findByIdOrNull(id)?.toDomain()
    }
}