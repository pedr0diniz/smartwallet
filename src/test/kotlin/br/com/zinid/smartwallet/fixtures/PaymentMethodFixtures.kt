package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods

object PaymentMethodFixtures {

    fun mockPaymentMethod(financialAccount: FinancialAccount) =
        PaymentMethod(
            id = 3L,
            method = PaymentMethods.DEBIT,
            financialAccount = financialAccount
        )
}
