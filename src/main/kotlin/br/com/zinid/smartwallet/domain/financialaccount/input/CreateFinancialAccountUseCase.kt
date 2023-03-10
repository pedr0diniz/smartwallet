package br.com.zinid.smartwallet.domain.financialaccount.input

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.CreateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort

class CreateFinancialAccountUseCase(
    private val findUserAdapter: FindUserOutputPort,
    private val createFinancialAccountAdapter: CreateFinancialAccountOutputPort
) : CreateFinancialAccountInputPort {

    override fun execute(financialAccount: FinancialAccount): Long? {
        val possibleUser = findUserAdapter.findById(financialAccount.user.id!!) ?: return null
        return createFinancialAccountAdapter.create(financialAccount.copy(user = possibleUser))
    }
}