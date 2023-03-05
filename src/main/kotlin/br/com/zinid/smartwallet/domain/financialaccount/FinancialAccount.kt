package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.user.User
import java.math.BigDecimal

data class FinancialAccount(
    val id: Long? = null,
    val institution: String? = null,
    var balance: BigDecimal? = null,
    val paymentMethods: List<PaymentMethod>? = listOf(),
    var overdraft: BigDecimal? = BigDecimal.ZERO,
    val user: User? = null
) {
    fun deduct(value: BigDecimal): Boolean {
        if ((balance!! - value) >= (- overdraft!!)) {
            balance = balance!! - value
            return true
        }

        return false
    }
}