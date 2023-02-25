package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import org.springframework.data.jpa.repository.JpaRepository

interface FinancialAccountRepository : JpaRepository<FinancialAccountEntity, Long> {
}