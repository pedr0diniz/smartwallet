package br.com.zinid.smartwallet.domain.paymentmethod.debit.input

import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

interface FindDebitPaymentMethodInputPort {
    fun findByFinancialAccountId(financialAccountId: Long): List<DebitPaymentMethod>
}
