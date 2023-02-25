package br.com.zinid.smartwallet.application.adapter.paymentmethod.input

import br.com.zinid.smartwallet.application.config.validation.ValueOfEnum
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class PaymentMethodRequest(
    @field:ValueOfEnum(enumClass = PaymentMethods::class)
    @field:NotBlank
    @field:Size(min = 2, max = 255)
    val method: String,

    @field:NotNull
    @field:Positive
    val financialAccountId: Long
) {
    fun toDomain() = PaymentMethod(
        method = method,
        financialAccount = FinancialAccount(id = financialAccountId)
    )
}