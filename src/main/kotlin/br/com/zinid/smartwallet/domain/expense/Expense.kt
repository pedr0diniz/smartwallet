package br.com.zinid.smartwallet.domain.expense

import br.com.zinid.smartwallet.domain.payment.Payment
import java.math.BigDecimal
import java.time.LocalDate

data class Expense(
    val id: Long,
    val content: String,
    val date: LocalDate,
    val price: BigDecimal,
    val payment: Payment,
    val essential: Boolean? = false
)