package br.com.zinid.smartwallet.application.adapter.creditcard.output

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.creditcard.output.FindCreditCardOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindCreditCardAdapter(
    val creditCardRepository: CreditCardRepository
) : FindCreditCardOutputPort {
    override fun findById(id: Long): CreditCard? =
        creditCardRepository.findByIdOrNull(id)?.toDomain()

    override fun findByPaymentMethodId(id: Long): CreditCard? =
        creditCardRepository.findByPaymentMethodId(id)?.toDomain()
}
