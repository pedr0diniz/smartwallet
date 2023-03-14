package br.com.zinid.smartwallet.domain.user.output

import br.com.zinid.smartwallet.domain.user.User

interface CreateUserOutputPort {
    fun create(user: User): User?
}