package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output

import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseResponse
import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodResponse
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

data class DebitPaymentMethodResponse(
    val id: Long?,
    val type: String,
    val expenses: List<DebitExpenseResponse>
) : PaymentMethodResponse {

    companion object {
        fun fromDomain(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethodResponse =
            DebitPaymentMethodResponse(
                id = debitPaymentMethod.id,
                type = debitPaymentMethod.type.name,
                expenses = debitPaymentMethod.expenses.map { DebitExpenseResponse.fromDomain(it) }
            )
    }
}
