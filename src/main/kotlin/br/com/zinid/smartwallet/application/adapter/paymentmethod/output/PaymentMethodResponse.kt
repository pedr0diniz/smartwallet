package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.application.adapter.creditcard.output.CreditCardResponse
import br.com.zinid.smartwallet.application.adapter.expense.output.ExpenseResponse
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod

data class PaymentMethodResponse(
    val id: Long?,
    val method: List<String>,
    val creditCard: CreditCardResponse? = null,
    val expenses: List<ExpenseResponse>? = listOf()
) {
    companion object {
        fun fromDomain(paymentMethod: PaymentMethod) = PaymentMethodResponse(
            id = paymentMethod.id,
            method = listOf(paymentMethod.method.toString()),
            creditCard = if (paymentMethod.creditCard != null) {
                CreditCardResponse.fromDomain(paymentMethod.creditCard!!)
            } else {
                null
            },
            expenses = paymentMethod.expenses?.map { ExpenseResponse.fromDomain(it) }
        )
    }
}