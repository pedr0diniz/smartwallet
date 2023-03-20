package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.creditcardinstallment.output.FindCreditCardInstallmentsOutputPort
import org.springframework.stereotype.Service

@Service
class FindCreditCardInstallmentsAdapter(
    private val creditCardInstallmentsRepository: CreditCardInstallmentsRepository
) : FindCreditCardInstallmentsOutputPort {

    override fun findByExpenseId(expenseId: Long): CreditCardInstallments? {
        return creditCardInstallmentsRepository.findByExpenseId(expenseId)?.toDomain()
    }
}