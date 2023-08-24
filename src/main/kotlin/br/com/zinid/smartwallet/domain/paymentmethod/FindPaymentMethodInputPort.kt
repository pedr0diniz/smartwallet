package br.com.zinid.smartwallet.domain.paymentmethod

interface FindPaymentMethodInputPort {
    fun findByFinancialAccountId(financialAccountId: Long): List<PaymentMethod>
}
