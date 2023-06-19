package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.CreateCreditCardOutputPort
import org.springframework.stereotype.Service

@Service
class CreateCreditCardAdapter(
    private val creditCardRepository: CreditCardRepository
) : CreateCreditCardOutputPort {

    // TODO - create payment method entity from here
    override fun create(creditCard: CreditCard): CreditCard? =
        creditCardRepository.save(CreditCardEntity.fromDomain(creditCard)).toDomain()
}
