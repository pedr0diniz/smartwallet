package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.user.User
import java.math.BigDecimal

data class FinancialAccount(
    val id: Long? = null,
    val institution: String,
    var balance: BigDecimal,
    val paymentMethods: List<PaymentMethod>? = listOf(),
    var overdraft: BigDecimal,
    val user: User
) {
    fun hasBalance(value: BigDecimal): Boolean {
        if (balance.subtract(value) >= overdraft.negate()) {
            balance -= value
            return true
        }

        return false
    }

    fun deductFromBalance(value: BigDecimal) {
        balance.subtract(value)
    }

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
    }
}