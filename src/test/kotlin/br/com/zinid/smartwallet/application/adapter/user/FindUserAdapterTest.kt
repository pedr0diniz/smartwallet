package br.com.zinid.smartwallet.application.adapter.user

import br.com.zinid.smartwallet.application.adapter.user.output.FindUserAdapter
import br.com.zinid.smartwallet.application.adapter.user.output.UserEntity
import br.com.zinid.smartwallet.application.adapter.user.output.UserRepository
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.UserFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class FindUserAdapterTest {

    private val userRepository = mockk<UserRepository>()
    private val findFinancialAccountAdapter = mockk<FindFinancialAccountOutputPort>()
    private val findUserAdapter = FindUserAdapter(
        userRepository,
        findFinancialAccountAdapter
    )

    @Test
    fun `should find user with financial accounts`() {
        val userId = 2L
        val user = UserFixtures.getUser().copy(id = userId)
        val userEntity = UserEntity.fromDomain(user)
        val financialAccounts = listOf(FinancialAccountFixtures.getFinancialAccount(user))

        every { userRepository.findByIdOrNull(userId) } returns userEntity
        every { findFinancialAccountAdapter.findByUserId(userId) } returns financialAccounts

        val possibleUser = findUserAdapter.findById(userId)
        val expectedResponse = user.copy(financialAccounts = financialAccounts)

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
        verify(exactly = 1) { findFinancialAccountAdapter.findByUserId(userId) }

        assertEquals(expectedResponse, possibleUser)
    }

    @Test
    fun `should find user with no financial accounts`() {
        val userId = 2L
        val user = UserFixtures.getUser().copy(id = userId)
        val userEntity = UserEntity.fromDomain(user)

        every { userRepository.findByIdOrNull(userId) } returns userEntity
        every { findFinancialAccountAdapter.findByUserId(userId) } returns emptyList()

        val possibleUser = findUserAdapter.findById(userId)

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
        verify(exactly = 1) { findFinancialAccountAdapter.findByUserId(userId) }

        assertEquals(user, possibleUser)
    }

    @Test
    fun `should not find user`() {
        val userId = 2L

        every { userRepository.findByIdOrNull(userId) } returns null

        assertNull(findUserAdapter.findById(userId))

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
    }
}
