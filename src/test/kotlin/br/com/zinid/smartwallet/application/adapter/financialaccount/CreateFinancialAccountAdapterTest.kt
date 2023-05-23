package br.com.zinid.smartwallet.application.adapter.financialaccount

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.CreateFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountRepository
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.UserFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateFinancialAccountAdapterTest {

    private val financialAccountRepository = mockk<FinancialAccountRepository>()
    private val createFinancialAccountAdapter = CreateFinancialAccountAdapter(financialAccountRepository)

    @Test
    fun `should create financial account`() {
        val financialAccount = FinancialAccountFixtures.mockFinancialAccount(
            user = UserFixtures.mockUser()
        )
        val financialAccountEntity = FinancialAccountEntity.fromDomain(financialAccount)

        every { financialAccountRepository.save(financialAccountEntity) } returns financialAccountEntity

        val createdFinancialAccount = createFinancialAccountAdapter.create(financialAccount)

        verify(exactly = 1) { financialAccountRepository.save(financialAccountEntity) }

        assertEquals(financialAccount, createdFinancialAccount)
    }
}
