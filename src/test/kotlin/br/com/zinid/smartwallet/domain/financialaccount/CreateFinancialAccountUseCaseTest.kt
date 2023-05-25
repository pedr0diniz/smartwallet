package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.financialaccount.input.CreateFinancialAccountUseCase
import br.com.zinid.smartwallet.domain.financialaccount.output.CreateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.user.User
import br.com.zinid.smartwallet.domain.user.output.FindUserOutputPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CreateFinancialAccountUseCaseTest {

    private val findUserAdapter = mockk<FindUserOutputPort>()
    private val createFinancialAccountAdapter = mockk<CreateFinancialAccountOutputPort>()
    private val createFinancialAccountUseCase = CreateFinancialAccountUseCase(
        findUserAdapter,
        createFinancialAccountAdapter
    )

    @Test
    fun `should find user and fail to create financial account`() {
        val id = 1L
        val userId = 2L
        val financialAccount = FinancialAccount.createBlankFromIdAndUserId(id, userId)

        every { findUserAdapter.findById(userId) } returns User.createBlankFromId(userId)
        every { createFinancialAccountAdapter.create(financialAccount) } returns financialAccount

        assertEquals(financialAccount, createFinancialAccountUseCase.execute(financialAccount))

        verify(exactly = 1) { findUserAdapter.findById(userId) }
        verify(exactly = 1) { createFinancialAccountAdapter.create(financialAccount) }
    }
    @Test
    fun `should not find user and create financial account`() {
        val id = 1L
        val userId = 2L
        val financialAccount = FinancialAccount.createBlankFromIdAndUserId(id, userId)

        every { findUserAdapter.findById(userId) } returns null

        assertNull(createFinancialAccountUseCase.execute(financialAccount))

        verify(exactly = 1) { findUserAdapter.findById(userId) }
    }
}
