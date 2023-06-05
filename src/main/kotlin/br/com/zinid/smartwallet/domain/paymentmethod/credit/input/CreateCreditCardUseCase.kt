package br.com.zinid.smartwallet.domain.paymentmethod.credit.input

import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.CreateCreditCardOutputPort

class CreateCreditCardUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val createCreditCardAdapter: CreateCreditCardOutputPort
) : CreateCreditCardInputPort {

    override fun execute(creditCard: CreditCard): CreditCard? {
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(creditCard.financialAccount.id!!)
            ?: return null

        return createCreditCardAdapter.create(creditCard.copy(financialAccount = possibleFinancialAccount))
    }
}
