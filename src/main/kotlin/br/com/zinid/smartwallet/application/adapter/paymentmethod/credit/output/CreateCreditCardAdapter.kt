package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodRepository
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.CreateCreditCardOutputPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CreateCreditCardAdapter(
    private val creditCardRepository: CreditCardRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : CreateCreditCardOutputPort {

    @Transactional
    override fun create(creditCard: CreditCard): CreditCard? {
        val creditCardEntity = creditCardRepository.save(CreditCardEntity.fromDomain(creditCard))

        paymentMethodRepository.save(PaymentMethodEntity.from(creditCardEntity))

        return creditCardEntity.toDomain()
    }
}
