package br.com.zinid.smartwallet.application.adapter.creditcard.input

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCardRequest(
    @field:Pattern(regexp="[\\d]{4}")
    @field:NotBlank
    val last4Digits: String,

    @field:NotBlank
    @field:JsonFormat(pattern = "YYYY-MM-DD")
    val expirationDate: String,

    @field:NotNull
    val cardLimit: BigDecimal,

    @field:NotNull
    @field:Min(1)
    @field:Max(31)
    val invoiceClosingDayOfMonth: Int
) {
    fun toDomain() = CreditCard(
        last4Digits = last4Digits,
        expirationDate = LocalDate.parse(expirationDate),
        cardLimit = cardLimit,
        invoiceClosingDayOfMonth = invoiceClosingDayOfMonth
    )
}