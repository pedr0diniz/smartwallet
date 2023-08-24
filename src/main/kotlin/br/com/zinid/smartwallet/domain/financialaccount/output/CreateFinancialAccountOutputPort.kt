package br.com.zinid.smartwallet.domain.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

interface CreateFinancialAccountOutputPort {

    fun create(financialAccount: FinancialAccount): FinancialAccount
}
