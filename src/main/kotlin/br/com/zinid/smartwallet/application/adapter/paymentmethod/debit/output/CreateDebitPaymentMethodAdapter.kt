package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodRepository
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.CreateDebitPaymentMethodOutputPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CreateDebitPaymentMethodAdapter(
    private val debitPaymentMethodRepository: DebitPaymentMethodRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : CreateDebitPaymentMethodOutputPort {

    @Transactional
    override fun create(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethod? {
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)
        val paymentMethodEntity = PaymentMethodEntity.from(debitPaymentMethodEntity)

        paymentMethodRepository.save(paymentMethodEntity)

        return debitPaymentMethodRepository.save(debitPaymentMethodEntity).toDomain()
    }
}
