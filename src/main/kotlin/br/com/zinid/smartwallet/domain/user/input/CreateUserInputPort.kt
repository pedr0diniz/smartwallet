package br.com.zinid.smartwallet.domain.user.input

import br.com.zinid.smartwallet.domain.user.User

interface CreateUserInputPort {
    fun execute(user: User): User?
}