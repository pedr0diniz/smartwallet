package br.com.zinid.smartwallet.domain.financialaccount.input

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort

class FindFinancialAccountUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort
) : FindFinancialAccountInputPort {
    override fun findByUserId(userId: Long): List<FinancialAccount> = findFinancialAccountAdapter.findByUserId(userId)
}
