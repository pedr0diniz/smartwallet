package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.CreateFinancialAccountOutputPort
import org.springframework.stereotype.Service

@Service
class CreateFinancialAccountAdapter(
    private val financialAccountRepository: FinancialAccountRepository
) : CreateFinancialAccountOutputPort {

    override fun create(financialAccount: FinancialAccount): FinancialAccount? {
        return financialAccountRepository.save(FinancialAccountEntity.fromDomain(financialAccount)).toDomain()
    }
}