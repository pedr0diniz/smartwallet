package br.com.zinid.smartwallet.domain.user

import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.user.input.FindUserUseCase
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort
import br.com.zinid.smartwallet.fixtures.UserFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class FindUserUseCaseTest {

    private val findUserAdapter = mockk<FindUserOutputPort>()
    private val findUserUseCase = FindUserUseCase(findUserAdapter)
    private val userId = 1L

    @Test
    fun `should find user by id`() {
        val user = UserFixtures.getUser()
        every { findUserAdapter.findById(userId) } returns user

        val response = findUserUseCase.findById(userId)

        assertEquals(response, user)

        verify { findUserAdapter.findById(userId) }
    }

    @Test
    fun `should not find user by id`() {
        every { findUserAdapter.findById(userId) } returns null

        assertThrows<NotFoundException> { findUserUseCase.findById(userId) }

        verify { findUserAdapter.findById(userId) }
    }
}
