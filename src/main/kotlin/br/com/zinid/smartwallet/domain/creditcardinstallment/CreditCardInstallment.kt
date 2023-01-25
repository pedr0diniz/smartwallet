package br.com.zinid.smartwallet.domain.creditcardinstallment

import br.com.zinid.smartwallet.domain.expense.Expense

data class CreditCardInstallment(
    val id: Long,
    val numberOfMonths: Int,
    val currentMonth: Int,
    val expense: Expense
)
