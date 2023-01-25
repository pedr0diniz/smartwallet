package br.com.zinid.smartwallet.domain.financialaccount

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.user.User
import java.math.BigDecimal

data class FinancialAccount(
    val id: Long? = null,
    val institution: String,
    val balance: BigDecimal,
    val paymentMethods: List<PaymentMethod>? = listOf(),
    val user: User
)