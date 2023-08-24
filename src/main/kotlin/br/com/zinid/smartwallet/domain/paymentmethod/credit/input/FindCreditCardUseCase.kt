package br.com.zinid.smartwallet.domain.paymentmethod.credit.input

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort

class FindCreditCardUseCase(
    private val findCreditCardAdapter: FindCreditCardOutputPort
) : FindCreditCardInputPort {
    override fun findByFinancialAccountId(financialAccountId: Long): List<CreditCard> =
        findCreditCardAdapter.findByFinancialAccountId(financialAccountId)
}
