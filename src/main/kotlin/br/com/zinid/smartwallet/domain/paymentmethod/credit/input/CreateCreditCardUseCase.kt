package br.com.zinid.smartwallet.domain.paymentmethod.credit.input

import br.com.zinid.smartwallet.domain.exception.DomainClasses.FINANCIAL_ACCOUNT
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.CreateCreditCardOutputPort

class CreateCreditCardUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val createCreditCardAdapter: CreateCreditCardOutputPort
) : CreateCreditCardInputPort {

    override fun execute(creditCard: CreditCard): CreditCard {
        val financialAccountId = creditCard.financialAccount.id!!
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(financialAccountId)
            ?: throw NotFoundException.buildFrom(FINANCIAL_ACCOUNT, "id", financialAccountId)

        return createCreditCardAdapter.create(creditCard.copy(financialAccount = possibleFinancialAccount))
    }
}
