package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output

import org.springframework.data.jpa.repository.JpaRepository

interface CreditCardRepository : JpaRepository<CreditCardEntity, Long> {

    fun findByFinancialAccountId(financialAccountId: Long): List<CreditCardEntity>
}
