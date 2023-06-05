package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

object PaymentMethodFixtures {

    fun getCreditPaymentMethod(financialAccount: FinancialAccount) =
        CreditCard(
            id = 3L,
            financialAccount = financialAccount,
            last4Digits = "1234",
            expirationDate = LocalDate.parse("2031-10-01"),
            cardLimit = BigDecimal.valueOf(10000L),
            invoiceClosingDayOfMonth = 1
        )

    fun getDebitPaymentMethod(financialAccount: FinancialAccount) =
        DebitPaymentMethod(
            id = 3L,
            type = PaymentType.DEBIT,
            financialAccount = financialAccount
        )
}
