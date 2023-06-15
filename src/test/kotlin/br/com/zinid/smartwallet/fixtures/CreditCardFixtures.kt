package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import java.math.BigDecimal
import java.time.LocalDate

object CreditCardFixtures {

    private val user = UserFixtures.getUser()
    private val financialAccount = FinancialAccountFixtures.getFinancialAccount(user)

    fun getCreditCard() = CreditCard(
        id = 2L,
        last4Digits = "1234",
        expirationDate = LocalDate.of(2031, 10, 1),
        cardLimit = BigDecimal.valueOf(10000L),
        financialAccount = financialAccount,
        expenses = emptyList(),
        invoiceClosingDayOfMonth = 25
    )
}
