package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindUserAdapter(
    private val userRepository: UserRepository,
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort
) : FindUserOutputPort {

    override fun findById(id: Long): User? {
        val possibleUser = userRepository.findByIdOrNull(id)?.toDomain()

        return possibleUser?.copy(
            financialAccounts = findFinancialAccountAdapter.findByUserId(id)
        )
    }
}
