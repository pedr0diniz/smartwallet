package br.com.zinid.smartwallet.domain.paymentmethod.debit.output

import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

interface FindDebitPaymentMethodOutputPort {

    fun findById(debitPaymentMethodId: Long): DebitPaymentMethod?

    fun findByFinancialAccountId(financialAccountId: Long): List<DebitPaymentMethod>
}
