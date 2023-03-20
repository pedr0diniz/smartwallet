package br.com.zinid.smartwallet.domain.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

interface FindFinancialAccountOutputPort {

    fun findById(id: Long): FinancialAccount?

    fun findByUserId(userId: Long): List<FinancialAccount>

}