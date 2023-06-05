package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output

import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.CreateDebitPaymentMethodOutputPort
import org.springframework.stereotype.Service

@Service
class CreateDebitPaymentMethodAdapter(
    private val debitPaymentMethodRepository: DebitPaymentMethodRepository
) : CreateDebitPaymentMethodOutputPort {

    override fun create(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethod? =
        debitPaymentMethodRepository.save(DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)).toDomain()
}
