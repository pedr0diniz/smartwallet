package br.com.zinid.smartwallet.domain.user.input

import br.com.zinid.smartwallet.domain.exception.DomainClasses.USER
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort

class FindUserUseCase(
    private val findUserAdapter: FindUserOutputPort
) : FindUserInputPort {
    override fun findById(userId: Long): User = findUserAdapter.findById(userId)
        ?: throw NotFoundException.buildFrom(USER, "id", userId)
}
