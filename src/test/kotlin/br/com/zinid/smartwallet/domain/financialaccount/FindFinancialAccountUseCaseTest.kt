package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.financialaccount.input.FindFinancialAccountUseCase
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindFinancialAccountUseCaseTest {

    private val findFinancialAccountAdapter = mockk<FindFinancialAccountOutputPort>()
    private val findFinancialAccountUseCase = FindFinancialAccountUseCase(findFinancialAccountAdapter)
    private val financialAccountId = 1L

    @Test
    fun `should find financial accounts by financialAccountId`() {
        val financialAccounts = listOf(FinancialAccountFixtures.getFinancialAccount())
        every { findFinancialAccountAdapter.findByUserId(financialAccountId) } returns financialAccounts

        val response = findFinancialAccountUseCase.findByUserId(financialAccountId)

        assertEquals(financialAccounts, response)
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `should not find financial accounts by financialAccountId`() {
        every { findFinancialAccountAdapter.findByUserId(financialAccountId) } returns emptyList()

        assertTrue(findFinancialAccountUseCase.findByUserId(financialAccountId).isEmpty())
    }
}
