package br.com.zinid.smartwallet.domain.financialaccount.input

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

interface CreateFinancialAccountInputPort {

    fun execute(financialAccount: FinancialAccount): Long?

}