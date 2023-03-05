package br.com.zinid.smartwallet.domain.creditcardinstallment

import br.com.zinid.smartwallet.domain.expense.Expense
import java.math.BigDecimal

data class CreditCardInstallment(
    val id: Long? = null,
    val numberOfMonths: Int? = null,
    val totalValue: BigDecimal? = null,
    val firstInstallmentValue: BigDecimal? = null,
    val installmentValue: BigDecimal? = null,
    val expense: Expense? = null,
)
