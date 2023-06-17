package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod

object DebitPaymentMethodFixtures {

    private val user = UserFixtures.getUser()
    private val fixtureAccount = FinancialAccountFixtures.getFinancialAccount(user)

    fun getDebitPaymentMethod(financialAccount: FinancialAccount? = fixtureAccount) = DebitPaymentMethod(
        id = 1L,
        type = PaymentType.DEBIT,
        financialAccount = financialAccount!!
    )
}
