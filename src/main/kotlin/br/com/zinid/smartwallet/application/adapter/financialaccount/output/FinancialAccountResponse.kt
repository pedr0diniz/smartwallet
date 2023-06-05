package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodResponse
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import java.math.BigDecimal

data class FinancialAccountResponse(
    val id: Long? = null,
    val institution: String,
    var balance: BigDecimal,
    val paymentMethods: List<PaymentMethodResponse>? = listOf(),
    val overdraft: BigDecimal,
) {
    companion object {
        fun fromDomain(financialAccount: FinancialAccount) = FinancialAccountResponse(
            id = financialAccount.id,
            institution = financialAccount.institution,
            balance = financialAccount.balance,
            paymentMethods = financialAccount.paymentMethods?.map { PaymentMethodResponse.fromDomain(it) },
            overdraft = financialAccount.overdraft
        )
    }
}
