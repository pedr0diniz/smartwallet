package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
import br.com.zinid.smartwallet.domain.user.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FinancialAccountTest {

    @Test
    fun `should create blank financial account`() {
        val financialAccount = FinancialAccount.createBlank()

        assert(financialAccount.id == 0L)
        assert(financialAccount.institution == "")
        assert(financialAccount.balance == BigDecimal.ZERO)
        assert(financialAccount.paymentMethods.isNullOrEmpty())
        assert(financialAccount.overdraft == BigDecimal.ZERO)
        assert(financialAccount.user == User.createBlank())
    }

    @Test
    fun `should create blank financial account from id`() {
        val id = 1L
        val financialAccount = FinancialAccount.createBlankFromId(id)

        assert(financialAccount.id == id)
        assert(financialAccount.institution == "")
        assert(financialAccount.balance == BigDecimal.ZERO)
        assert(financialAccount.paymentMethods.isNullOrEmpty())
        assert(financialAccount.overdraft == BigDecimal.ZERO)
        assert(financialAccount.user == User.createBlank())
    }

    @Test
    fun `should create blank financial account from id and user id`() {
        val id = 1L
        val userId = 2L
        val financialAccount = FinancialAccount.createBlankFromIdAndUserId(id, userId)

        assert(financialAccount.id == id)
        assert(financialAccount.institution == "")
        assert(financialAccount.balance == BigDecimal.ZERO)
        assert(financialAccount.paymentMethods.isNullOrEmpty())
        assert(financialAccount.overdraft == BigDecimal.ZERO)
        assert(financialAccount.user == User.createBlankFromId(userId))
    }

    @Test
    fun `should indicate that there is enough balance when account has overdraft`() {
        val financialAccount = FinancialAccount.createBlank()
            .copy(
                balance = BigDecimal.valueOf(100.00),
                overdraft = BigDecimal.valueOf(50.00)
            )

        assertTrue(financialAccount.hasBalance(BigDecimal.valueOf(150.00)))
    }

    @Test
    fun `should indicate that there is enough balance when account has no overdraft`() {
        val financialAccount = FinancialAccount.createBlank()
            .copy(
                balance = BigDecimal.valueOf(100.00),
                overdraft = BigDecimal.ZERO
            )

        assertFalse(financialAccount.hasBalance(BigDecimal.valueOf(150.00)))
    }

    @Test
    fun `should deduct from balance and keep it positive`() {
        val financialAccount = FinancialAccount.createBlank()
            .copy(
                balance = BigDecimal.valueOf(100.00),
                overdraft = BigDecimal.ZERO
            )
        val randomValue = BigDecimal.valueOf(Random.nextDouble(0.0, 100.0)).also { println(it) }

        financialAccount.deductFromBalance(randomValue)

        assertTrue(financialAccount.balance >= BigDecimal.ZERO)
    }

    @Test
    fun `should deduct from balance and use the overdraft`() {
        val overdraft = BigDecimal.valueOf(50.0)
        val financialAccount = FinancialAccount.createBlank()
            .copy(
                balance = BigDecimal.valueOf(100.00),
                overdraft = overdraft
            )
        val withdrawalValue = BigDecimal.valueOf(150.0)

        assertTrue(financialAccount.hasBalance(withdrawalValue))

        financialAccount.deductFromBalance(withdrawalValue)

        assertEquals(overdraft.negate(), financialAccount.balance)
    }

    @Test
    fun `should not deduct beyond overdraft`() {
        val originalBalance = BigDecimal.valueOf(100.00)
        val financialAccount = FinancialAccount.createBlank()
            .copy(
                balance = originalBalance,
                overdraft = BigDecimal.valueOf(50.00)
            )
        val withdrawalValue = BigDecimal.valueOf(151.0)

        assertThrows<InsufficientBalanceException> { financialAccount.deductFromBalance(withdrawalValue) }

        assertFalse(financialAccount.hasBalance(withdrawalValue))
    }

    @Test
    fun `should get remaining spendable value`() {
        val balance = BigDecimal.valueOf(100.00)
        val overdraft = BigDecimal.valueOf(50.00)
        val financialAccount = FinancialAccount.createBlank().copy(
            balance = balance,
            overdraft = overdraft
        )

        assertEquals(balance.add(overdraft), financialAccount.getRemainingSpendableValue())
    }
}
