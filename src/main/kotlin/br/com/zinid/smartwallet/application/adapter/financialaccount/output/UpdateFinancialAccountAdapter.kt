package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.UpdateFinancialAccountOutputPort
import org.springframework.stereotype.Service

@Service
class UpdateFinancialAccountAdapter(
    val financialAccountRepository: FinancialAccountRepository
) : UpdateFinancialAccountOutputPort {
    override fun update(financialAccount: FinancialAccount): Long? {
        return financialAccountRepository.save(FinancialAccountEntity.fromDomain(financialAccount)).id
    }
}