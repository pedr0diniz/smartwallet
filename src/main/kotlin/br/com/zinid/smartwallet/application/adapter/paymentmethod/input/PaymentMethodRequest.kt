package br.com.zinid.smartwallet.application.adapter.paymentmethod.input

import br.com.zinid.smartwallet.application.adapter.creditcard.input.CreditCardRequest
import br.com.zinid.smartwallet.application.config.validation.CollectionOfEnumValues
import br.com.zinid.smartwallet.application.config.validation.CreditMethodMustHaveACard
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@CreditMethodMustHaveACard
data class PaymentMethodRequest(

    @field:CollectionOfEnumValues(enumClass = PaymentMethods::class)
    val methods: Set<String>,

    @field:NotNull
    @field:Positive
    val financialAccountId: Long,

    @field:Valid
    val creditCard: CreditCardRequest? = null
) {

    fun toDomain(): List<PaymentMethod> {
        return methods.map {
            PaymentMethod(
                method = PaymentMethods.valueOf(it),
                financialAccount = FinancialAccount.createBlankFromId(id = financialAccountId)
            ).apply {
                if (this.method == PaymentMethods.CREDIT) {
                    this.creditCard = this@PaymentMethodRequest.creditCard?.toDomain(this)
                }
            }
        }
    }
}
