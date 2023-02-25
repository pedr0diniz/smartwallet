package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindFinancialAccountAdapter(
    val financialAccountRepository: FinancialAccountRepository
) : FindFinancialAccountOutputPort {
    override fun findById(id: Long): FinancialAccount? {
        return financialAccountRepository.findByIdOrNull(id)?.toDomain()
    }
}