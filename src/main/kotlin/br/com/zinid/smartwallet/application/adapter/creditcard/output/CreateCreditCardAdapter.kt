package br.com.zinid.smartwallet.application.adapter.creditcard.output

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.creditcard.output.CreateCreditCardOutputPort
import org.springframework.stereotype.Service

@Service
class CreateCreditCardAdapter(
    private val creditCardRepository: CreditCardRepository
) : CreateCreditCardOutputPort {

    override fun create(creditCard: CreditCard): Long? {
        return creditCardRepository.save(CreditCardEntity.fromDomain(creditCard)).id
    }
}