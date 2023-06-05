package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output

import org.springframework.data.jpa.repository.JpaRepository

interface DebitPaymentMethodRepository : JpaRepository<DebitPaymentMethodEntity, Long> {

    fun findByFinancialAccountId(financialAccountId: Long): List<DebitPaymentMethodEntity>
}
