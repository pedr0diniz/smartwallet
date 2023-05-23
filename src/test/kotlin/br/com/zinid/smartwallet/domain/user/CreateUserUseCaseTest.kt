package br.com.zinid.smartwallet.domain.user

import br.com.zinid.smartwallet.domain.user.input.CreateUserUseCase
import br.com.zinid.smartwallet.domain.user.output.CreateUserOutputPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateUserUseCaseTest {

    private val createUserAdapter = mockk<CreateUserOutputPort>()
    private val createUserUseCase = CreateUserUseCase(createUserAdapter)

    @Test
    fun `should create user`() {
        val user = User(
            id = 1L,
            firstname = "User",
            lastname = "from Test",
            email = "user@user.com",
            phone = "+551199999-6666"
        )

        every { createUserAdapter.create(user) } returns user

        val createdUser = createUserUseCase.execute(user)

        verify(exactly = 1) { createUserAdapter.create(user) }

        assertEquals(user, createdUser)
    }

}