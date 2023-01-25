package br.com.zinid.smartwallet.domain.creditcard

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCard(
    val id: Long,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val limit: BigDecimal,
    val installments: List<CreditCardInstallment>,
    val invoiceClosingDate: LocalDate
)
