package br.com.zinid.smartwallet.application.adapter.financialaccount.input

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.user.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class FinancialAccountRequest(
//    @field:ValueOfEnum
    @field:NotBlank
    val institution: String,
    val balance: BigDecimal? = BigDecimal.ZERO,

    @field:NotNull
    @field:Positive
    val userId: Long
) {
    fun toDomain() = FinancialAccount(
        institution = institution,
        balance = balance!!,
        user = User(1, "Pedro", "Diniz", "pedro@pedro.pedro", "12345678")
    )
}