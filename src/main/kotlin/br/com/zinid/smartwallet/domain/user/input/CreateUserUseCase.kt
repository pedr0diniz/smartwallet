package br.com.zinid.smartwallet.domain.user.input

import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.CreateUserOutputPort

class CreateUserUseCase(
    private val createUserAdapter: CreateUserOutputPort
) : CreateUserInputPort {

    override fun execute(user: User): User? {
        return createUserAdapter.create(user)
    }
}