package br.com.zinid.smartwallet.domain.financialaccount.input

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

interface FindFinancialAccountInputPort {
    fun findByUserId(userId: Long): List<FinancialAccount>
}
