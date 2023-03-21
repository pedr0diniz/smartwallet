package br.com.zinid.smartwallet.domain.user.output

import br.com.zinid.smartwallet.domain.user.User

interface FindUserOutputPort {
    fun findById(id: Long): User?
}
