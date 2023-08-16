package br.com.zinid.smartwallet.domain.user

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount

data class User(
    val id: Long? = null,
    val firstname: String,
    val lastname: String,
    val email: String,
    val phone: String,
    val financialAccounts: List<FinancialAccount>? = listOf()
) {
    companion object {
        fun createBlank() = User(
            id = 0L,
            firstname = "",
            lastname = "",
            email = "",
            phone = ""
        )
        fun createBlankFromId(id: Long) = User(
            id = id,
            firstname = "",
            lastname = "",
            email = "",
            phone = ""
        )
    }
}
