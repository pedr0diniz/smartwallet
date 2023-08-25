package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.user.User
import java.math.BigDecimal

data class FinancialAccount(
    val id: Long = 0L,
    val institution: String,
    var balance: BigDecimal,
    val paymentMethods: List<PaymentMethod> = listOf(),
    var overdraft: BigDecimal,
    val user: User
) {
    fun hasBalance(value: BigDecimal): Boolean =
        balance.subtract(value) >= overdraft.negate()

    fun deductFromBalance(value: BigDecimal) {
        if (hasBalance(value)) {
            balance = balance.subtract(value)
            return
        }

        throw InsufficientBalanceException(INSUFFICIENT_BALANCE_MESSAGE.format(value, getRemainingSpendableValue()))
    }

    fun getRemainingSpendableValue(): BigDecimal = balance.add(overdraft)

    companion object {
        fun createBlank() = FinancialAccount(
            id = 0L,
            institution = "",
            balance = BigDecimal.ZERO,
            paymentMethods = listOf(),
            overdraft = BigDecimal.ZERO,
            user = User.createBlank()
        )
        fun createBlankFromId(id: Long) = FinancialAccount(
            id = id,
            institution = "",
            balance = BigDecimal.ZERO,
            paymentMethods = listOf(),
            overdraft = BigDecimal.ZERO,
            user = User.createBlank()
        )

        fun createBlankFromIdAndUserId(id: Long, userId: Long) = FinancialAccount(
            id = id,
            institution = "",
            balance = BigDecimal.ZERO,
            paymentMethods = listOf(),
            overdraft = BigDecimal.ZERO,
            user = User.createBlankFromId(userId)
        )

        private const val INSUFFICIENT_BALANCE_MESSAGE = "Cannot deduct [R$ %s] from balance + overdraft of [R$ %s]"
    }
}
