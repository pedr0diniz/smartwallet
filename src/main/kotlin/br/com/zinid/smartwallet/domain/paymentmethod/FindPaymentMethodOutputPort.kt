package br.com.zinid.smartwallet.domain.paymentmethod

interface FindPaymentMethodOutputPort {

    fun findById(id: Long): PaymentMethod?

    fun findByCreditCardId(creditCardId: Long): PaymentMethod?

    fun findByDebitPaymentMethodId(debitPaymentMethodId: Long): PaymentMethod?

    fun findByFinancialAccountId(financialAccountId: Long): List<PaymentMethod>
}
