package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.output.CreatePaymentMethodOutputPort
import org.springframework.stereotype.Service

@Service
class CreatePaymentMethodAdapter(
    private val paymentMethodRepository: PaymentMethodRepository
) : CreatePaymentMethodOutputPort {

    override fun create(paymentMethod: PaymentMethod): PaymentMethod? =
        paymentMethodRepository.save(PaymentMethodEntity.fromDomain(paymentMethod)).toDomain()
}
