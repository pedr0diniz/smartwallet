package br.com.zinid.smartwallet.domain.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

interface UpdateFinancialAccountOutputPort {

    fun update(financialAccount: FinancialAccount): Long?

}