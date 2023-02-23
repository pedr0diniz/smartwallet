package br.com.zinid.smartwallet.domain.user

import br.com.zinid.smartwallet.domain.acquaintance.Acquaintance
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

data class User(
    val id: Long? = null,
    val firstname: String,
    val lastname: String,
    val email: String,
    val phone: String,
    val financialAccounts: List<FinancialAccount>? = listOf(),
    val acquaintances: List<Acquaintance>? = listOf()
) {

}