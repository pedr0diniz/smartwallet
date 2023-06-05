package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.input

import br.com.zinid.smartwallet.application.config.validation.CollectionOfEnumValues
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class DebitPaymentMethodRequest(
    @field:CollectionOfEnumValues(enumClass = PaymentType::class)
    val types: Set<String>,

    @field:NotNull
    @field:Positive
    val financialAccountId: Long,
) {

    fun toDomainList(): List<DebitPaymentMethod> {
        return types.map {
            DebitPaymentMethod(
                type = PaymentType.valueOf(it),
                financialAccount = FinancialAccount.createBlankFromId(id = financialAccountId)
            )
        }
    }
}
