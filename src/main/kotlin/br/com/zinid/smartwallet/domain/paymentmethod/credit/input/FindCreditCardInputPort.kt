package br.com.zinid.smartwallet.domain.paymentmethod.credit.input

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard

interface FindCreditCardInputPort {
    fun findByFinancialAccountId(financialAccountId: Long): List<CreditCard>
}
