package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.CreateUserOutputPort
import org.springframework.stereotype.Service

@Service
class CreateUserAdapter(
    private val userRepository: UserRepository
) : CreateUserOutputPort {

    override fun create(user: User): User? {
        return userRepository.save(UserEntity.fromDomain(user)).toDomain()
    }
}