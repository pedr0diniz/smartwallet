package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import org.springframework.stereotype.Service

@Service
class CreateCreditCardInstallmentsAdapter(
    private val creditCardInstallmentsRepository: CreditCardInstallmentsRepository
) : CreateCreditCardInstallmentsOutputPort {

    override fun createFromExpense(creditExpense: CreditExpense): CreditCardInstallments? {
        val creditCardInstallments = creditExpense.creditCardInstallments ?: return null
        return creditCardInstallmentsRepository.save(
            CreditCardInstallmentsEntity.fromDomain(creditCardInstallments)
        ).toDomain()
    }
}
