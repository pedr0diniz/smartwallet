package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import org.springframework.stereotype.Service

@Service
class CreateCreditCardInstallmentsAdapter(
    private val creditCardInstallmentsRepository: CreditCardInstallmentsRepository
) : CreateCreditCardInstallmentsOutputPort {

    override fun create(creditCardInstallments: CreditCardInstallments): CreditCardInstallments? {
        return creditCardInstallmentsRepository.save(
            CreditCardInstallmentsEntity.fromDomain(creditCardInstallments)
        ).toDomain()
    }
}