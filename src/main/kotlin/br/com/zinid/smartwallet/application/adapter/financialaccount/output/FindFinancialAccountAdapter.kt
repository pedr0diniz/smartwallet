package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindFinancialAccountAdapter(
    val financialAccountRepository: FinancialAccountRepository,
    val findPaymentMethodAdapter: FindPaymentMethodOutputPort
) : FindFinancialAccountOutputPort {
    override fun findById(id: Long): FinancialAccount? {
        val possibleFinancialAccount = financialAccountRepository.findByIdOrNull(id)?.toDomain()

        return possibleFinancialAccount?.copy(
            paymentMethods = getPaymentMethods(id)
        )
    }

    override fun findByUserId(userId: Long): List<FinancialAccount> {
        return financialAccountRepository.findByUserId(userId).map {
            it.toDomain().copy(
                paymentMethods = getPaymentMethods(it.id!!)
            )
        }
    }

    private fun getPaymentMethods(financialAccountId: Long) =
        findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId)
}