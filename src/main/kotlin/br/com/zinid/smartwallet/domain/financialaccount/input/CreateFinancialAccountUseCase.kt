package br.com.zinid.smartwallet.domain.financialaccount.input

import br.com.zinid.smartwallet.domain.exception.DomainClasses.USER
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.CreateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort

class CreateFinancialAccountUseCase(
    private val findUserAdapter: FindUserOutputPort,
    private val createFinancialAccountAdapter: CreateFinancialAccountOutputPort
) : CreateFinancialAccountInputPort {

    override fun execute(financialAccount: FinancialAccount): FinancialAccount {
        val userId = financialAccount.user.id
        val possibleUser = findUserAdapter.findById(userId)
            ?: throw NotFoundException.buildFrom(USER, "id", userId)

        return createFinancialAccountAdapter.create(financialAccount.copy(user = possibleUser))
    }
}
