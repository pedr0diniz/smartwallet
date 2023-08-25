package br.com.zinid.smartwallet.domain.user.input

import br.com.zinid.smartwallet.domain.user.User

interface FindUserInputPort {
    fun findById(userId: Long): User?
}
