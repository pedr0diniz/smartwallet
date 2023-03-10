package br.com.zinid.smartwallet.application.adapter.paymentmethod.input

import br.com.zinid.smartwallet.application.adapter.creditcard.input.CreditCardRequest
import br.com.zinid.smartwallet.application.config.validation.ValueOfEnum
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
import jakarta.validation.Valid
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
    val financialAccountId: Long,

    @field:Valid
    val creditCard: CreditCardRequest? = null
) {
    fun toDomain() = PaymentMethod(
        method = PaymentMethods.valueOf(method),
        financialAccount = FinancialAccount.createBlankFromId(id = financialAccountId)
    ).apply {
        this.creditCard = this@PaymentMethodRequest.creditCard?.toDomain(this)
    }
}