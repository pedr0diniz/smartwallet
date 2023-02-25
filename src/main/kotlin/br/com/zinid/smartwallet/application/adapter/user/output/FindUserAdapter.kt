package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class FindUserAdapter(
    private val userRepository: UserRepository
) : FindUserOutputPort {

    override fun findById(id: Long): User? {
        return userRepository.findByIdOrNull(id)?.toDomain()
    }
}