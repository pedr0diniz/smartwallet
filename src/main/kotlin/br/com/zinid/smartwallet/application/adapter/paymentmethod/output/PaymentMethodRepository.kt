package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import org.springframework.data.jpa.repository.JpaRepository

interface PaymentMethodRepository : JpaRepository<PaymentMethodEntity, Long> {

    fun findByFinancialAccountId(financialAccountId: Long): List<PaymentMethodEntity>
}
