package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.FindPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindFinancialAccountAdapter(
    private val financialAccountRepository: FinancialAccountRepository,
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort
) : FindFinancialAccountOutputPort {

    override fun findById(id: Long): FinancialAccount? {
        val possibleFinancialAccount = financialAccountRepository.findByIdOrNull(id)?.toDomain()
        // TODO - receber os paymentMethods como parâmetro do toDomain()
        // Tirar o .toDomain() da chamada acima

        // Colocar o .toDomain() no lugar do copy na chamada abaixo recebendo o mesmo parâmetro
        // Igual ao findCreditCardAdapter
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
