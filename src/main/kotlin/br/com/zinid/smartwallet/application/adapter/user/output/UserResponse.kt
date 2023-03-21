package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountResponse
import br.com.zinid.smartwallet.domain.user.User

data class UserResponse(
    val id: Long? = null,
    val firstname: String,
    val lastname: String,
    val email: String,
    val phone: String,
    val financialAccounts: List<FinancialAccountResponse>? = listOf()
) {
    companion object {
        fun fromDomain(user: User) = UserResponse(
            id = user.id,
            firstname = user.firstname,
            lastname = user.lastname,
            email = user.email,
            phone = user.phone,
            financialAccounts = user.financialAccounts?.map { FinancialAccountResponse.fromDomain(it) }
        )
    }
}
