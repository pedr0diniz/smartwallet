package br.com.zinid.smartwallet.application.adapter.financialaccount

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountRepository
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.UpdateFinancialAccountAdapter
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.UserFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class UpdateFinancialAccountAdapterTest {

    private val financialAccountRepository = mockk<FinancialAccountRepository>()
    private val updateFinancialAccountAdapter = UpdateFinancialAccountAdapter(financialAccountRepository)

    @Test
    fun `should update financial account`() {
        val financialAccount = FinancialAccountFixtures.getFinancialAccount(
            user = UserFixtures.getUser()
        )
        val financialAccountEntity = FinancialAccountEntity.fromDomain(financialAccount)

        every { financialAccountRepository.save(financialAccountEntity) } returns financialAccountEntity

        val updatedFinancialAccount = updateFinancialAccountAdapter.update(financialAccount)

        verify(exactly = 1) { financialAccountRepository.save(financialAccountEntity) }

        assertEquals(financialAccount, updatedFinancialAccount)
    }
}
