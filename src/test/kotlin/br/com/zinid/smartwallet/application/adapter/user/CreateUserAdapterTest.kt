package br.com.zinid.smartwallet.application.adapter.user

import br.com.zinid.smartwallet.application.adapter.user.output.CreateUserAdapter
import br.com.zinid.smartwallet.application.adapter.user.output.UserEntity
import br.com.zinid.smartwallet.application.adapter.user.output.UserRepository
import br.com.zinid.smartwallet.domain.user.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateUserAdapterTest {

    private val userRepository = mockk<UserRepository>()
    private val createUserAdapter = CreateUserAdapter(userRepository)

    @Test
    fun `should create user`() {
        val user = User(
            id = 1L,
            firstname = "User",
            lastname = "from Test",
            email = "user@user.com",
            phone = "+551199999-6666"
        )
        val userEntity = UserEntity.fromDomain(user)

        every { userRepository.save(userEntity) } returns userEntity

        val createdUser = createUserAdapter.create(user)

        verify(exactly = 1) { userRepository.save(userEntity) }

        assertEquals(user, createdUser)
    }
}