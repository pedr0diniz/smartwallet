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
    fun hasBalance(value: BigDecimal): Boolean {
        if ((balance!! - value) >= (- overdraft!!)) {
            balance = balance!! - value
            return true
        }

        return false
    }

    fun deductFromBalance(value: BigDecimal) {
        balance!!.subtract(value)
    }

//    companion object {
//        fun createBlank() = FinancialAccount(
//            id = 0L,
//            institution = "",
//            balance = BigDecimal.ZERO,
//            paymentMethods = listOf(),
//            overdraft = BigDecimal.ZERO,
//            user = User.createBlank()
//        )
//        fun createBlankFromId(id: Long) = FinancialAccount(
//            id = id,
//            institution = "",
//            balance = BigDecimal.ZERO,
//            paymentMethods = listOf(),
//            overdraft = BigDecimal.ZERO,
//            user = User.createBlank()
//        )
//    }
}