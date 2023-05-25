package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

object CreditCardFixtures {

    fun getCreditCard(paymentMethod: PaymentMethod) = CreditCard(
        id = 4L,
        last4Digits = "1234",
        expirationDate = LocalDate.of(2031, 10, 1),
        cardLimit = BigDecimal.valueOf(10000.0),
        invoiceClosingDayOfMonth = 25,
        paymentMethod = paymentMethod
    )
}