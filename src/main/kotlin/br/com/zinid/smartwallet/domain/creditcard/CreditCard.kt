package br.com.zinid.smartwallet.domain.creditcard

import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCard(
    val id: Long? = null,
    val last4Digits: String,
    val expirationDate: LocalDate,
    val cardLimit: BigDecimal,
    val invoiceClosingDayOfMonth: Int,
    val paymentMethod: PaymentMethod
) {
    val currentInvoiceClosingDate: LocalDate = getCurrentClosingDate()
    val previousInvoiceClosingDate: LocalDate = getPreviousClosingDate()

    fun hasLimit(
        consumedLimit: BigDecimal,
        expenseValue: BigDecimal
    ): Boolean = (cardLimit >= consumedLimit.add(expenseValue))

    private fun getPreviousClosingDate(): LocalDate {
        val today = LocalDate.now()

        if (invoiceClosingDayOfMonth > today.dayOfMonth) {
            return getClosingDateWithValidDay(today.minusMonths(1), invoiceClosingDayOfMonth)
        }

        return getClosingDateWithValidDay(today, invoiceClosingDayOfMonth)
    }

    private fun getCurrentClosingDate(): LocalDate {
        val today = LocalDate.now()

        if (invoiceClosingDayOfMonth > today.dayOfMonth) {
            return getClosingDateWithValidDay(today, invoiceClosingDayOfMonth)
        }

        return getClosingDateWithValidDay(today.plusMonths(1), invoiceClosingDayOfMonth)
    }

    private fun getClosingDateWithValidDay(date: LocalDate, possibleClosingDay: Int): LocalDate {
        val lastDayOfMonth = date.month.length(date.isLeapYear)
        if (possibleClosingDay > lastDayOfMonth) {
            return date.withDayOfMonth(lastDayOfMonth)
        }
        return date.withDayOfMonth(possibleClosingDay)
    }
}
