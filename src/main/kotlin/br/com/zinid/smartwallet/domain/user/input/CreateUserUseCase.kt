package br.com.zinid.smartwallet.domain.user.input

import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.CreateUserOutputPort

class CreateUserUseCase(
    private val createAuthorAdapter: CreateUserOutputPort
) : CreateUserInputPort {

    override fun execute(user: User): Long? {
        return createAuthorAdapter.create(user)
    }
}