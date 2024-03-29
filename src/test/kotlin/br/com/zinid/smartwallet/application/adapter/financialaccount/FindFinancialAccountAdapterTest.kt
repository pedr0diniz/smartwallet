package br.com.zinid.smartwallet.application.adapter.financialaccount

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountRepository
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FindFinancialAccountAdapter
import br.com.zinid.smartwallet.domain.paymentmethod.FindPaymentMethodOutputPort
import br.com.zinid.smartwallet.fixtures.FinancialAccountFixtures
import br.com.zinid.smartwallet.fixtures.PaymentMethodFixtures
import br.com.zinid.smartwallet.fixtures.UserFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class FindFinancialAccountAdapterTest {

    private val financialAccountRepository = mockk<FinancialAccountRepository>()
    private val findPaymentMethodAdapter = mockk<FindPaymentMethodOutputPort>()
    private val findFinancialAccountAdapter = FindFinancialAccountAdapter(
        financialAccountRepository,
        findPaymentMethodAdapter
    )

    @Test
    fun `should find financial accounts with payment methods by id`() {
        val financialAccountId = 1L
        val user = UserFixtures.getUser()
        val financialAccount = FinancialAccountFixtures.getFinancialAccount(user).copy(id = financialAccountId)
        val financialAccountEntity = FinancialAccountEntity.fromDomain(financialAccount)
        val paymentMethods = listOf(PaymentMethodFixtures.getCreditPaymentMethod(financialAccount))

        every { financialAccountRepository.findByIdOrNull(financialAccountId) } returns financialAccountEntity
        every { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns paymentMethods

        val possibleFinancialAccount = findFinancialAccountAdapter.findById(financialAccountId)
        val expectedResponse = financialAccount.copy(paymentMethods = paymentMethods)

        verify(exactly = 1) { financialAccountRepository.findByIdOrNull(financialAccountId) }
        verify(exactly = 1) { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) }

        assertEquals(expectedResponse, possibleFinancialAccount)
    }

    @Test
    fun `should find financial accounts with no payment methods by id`() {
        val financialAccountId = 1L
        val user = UserFixtures.getUser()
        val financialAccount = FinancialAccountFixtures.getFinancialAccount(user).copy(id = financialAccountId)
        val financialAccountEntity = FinancialAccountEntity.fromDomain(financialAccount)

        every { financialAccountRepository.findByIdOrNull(financialAccountId) } returns financialAccountEntity
        every { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) } returns emptyList()

        val possibleFinancialAccount = findFinancialAccountAdapter.findById(financialAccountId)

        verify(exactly = 1) { financialAccountRepository.findByIdOrNull(financialAccountId) }
        verify(exactly = 1) { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountId) }

        assertEquals(financialAccount, possibleFinancialAccount)
    }

    @Test
    fun `should not find financial accounts by id`() {
        val financialAccountId = 1L

        every { financialAccountRepository.findByIdOrNull(financialAccountId) } returns null

        assertNull(findFinancialAccountAdapter.findById(financialAccountId))

        verify(exactly = 1) { financialAccountRepository.findByIdOrNull(financialAccountId) }
    }

    @Test
    fun `should find financial accounts with payment methods by user id`() {
        val userId = 2L
        val user = UserFixtures.getUser().copy(id = userId)
        val financialAccount = FinancialAccountFixtures.getFinancialAccount(user)
        val financialAccountEntity = FinancialAccountEntity.fromDomain(financialAccount)
        val paymentMethods = listOf(PaymentMethodFixtures.getDebitPaymentMethod(financialAccount))

        every { financialAccountRepository.findByUserId(userId) } returns listOf(financialAccountEntity)
        every { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountEntity.id!!) } returns paymentMethods

        val possibleFinancialAccount = findFinancialAccountAdapter.findByUserId(userId)
        val expectedResponse = listOf(financialAccount.copy(paymentMethods = paymentMethods))

        verify(exactly = 1) { financialAccountRepository.findByUserId(userId) }
        verify(exactly = 1) { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountEntity.id!!) }

        assertEquals(expectedResponse, possibleFinancialAccount)
    }

    @Test
    fun `should find financial accounts with no payment methods by user id`() {
        val userId = 2L
        val user = UserFixtures.getUser().copy(id = userId)
        val financialAccount = FinancialAccountFixtures.getFinancialAccount(user)
        val financialAccountEntity = FinancialAccountEntity.fromDomain(financialAccount)

        every { financialAccountRepository.findByUserId(userId) } returns listOf(financialAccountEntity)
        every { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountEntity.id!!) } returns emptyList()

        val possibleFinancialAccount = findFinancialAccountAdapter.findByUserId(userId)

        verify(exactly = 1) { financialAccountRepository.findByUserId(userId) }
        verify(exactly = 1) { findPaymentMethodAdapter.findByFinancialAccountId(financialAccountEntity.id!!) }

        assertEquals(listOf(financialAccount), possibleFinancialAccount)
    }

    @Test
    fun `should not find financial accounts by user id`() {
        val userId = 2L

        every { financialAccountRepository.findByUserId(userId) } returns emptyList()

        assert(findFinancialAccountAdapter.findByUserId(userId).isEmpty())

        verify(exactly = 1) { financialAccountRepository.findByUserId(userId) }
    }
}
