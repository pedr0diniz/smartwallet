package br.com.zinid.smartwallet.domain.paymentmethod.credit.output

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard

interface FindCreditCardOutputPort {

    fun findById(creditCardId: Long): CreditCard?

    fun findByFinancialAccountId(financialAccountId: Long): List<CreditCard>
}
