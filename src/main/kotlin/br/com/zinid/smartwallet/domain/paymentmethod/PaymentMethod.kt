package br.com.zinid.smartwallet.domain.paymentmethod

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

data class PaymentMethod(
    val id: Long? = null,
    val method: String? = null, // pix, débito, crédito, va, vr, etc
    val creditCard: CreditCard? = null,
    val financialAccount: FinancialAccount? = null,
    val expenses: List<Expense>? = listOf()
)
