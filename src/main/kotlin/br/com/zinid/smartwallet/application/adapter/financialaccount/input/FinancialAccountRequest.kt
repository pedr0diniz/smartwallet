package br.com.zinid.smartwallet.application.adapter.financialaccount.input

import br.com.zinid.smartwallet.application.config.validation.ValueOfEnum
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.Institutions
import br.com.zinid.smartwallet.domain.user.User
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class FinancialAccountRequest(
    @field:ValueOfEnum(enumClass = Institutions::class)
    @field:NotBlank
    @field:Size(min = 2, max = 255)
    val institution: String,

    @field:NotNull
    @field:Positive
    val userId: Long,

    val balance: BigDecimal? = BigDecimal.ZERO,

    @field:DecimalMin(value = "0.0")
    val overdraft: BigDecimal? = BigDecimal.ZERO
) {
    fun toDomain() = FinancialAccount(
        institution = institution,
        balance = balance!!,
        overdraft = overdraft!!,
        user = User.createBlankFromId(id = userId)
    )
}