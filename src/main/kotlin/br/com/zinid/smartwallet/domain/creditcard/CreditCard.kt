package br.com.zinid.smartwallet.domain.creditcard

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
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
    val previousInvoiceClosingDate: LocalDate = getLastClosingDate()

    fun hasLimit(
        consumedLimit: BigDecimal,
        expenseValue: BigDecimal
    ): Boolean = (cardLimit >= consumedLimit.add(expenseValue))

    private fun getLastClosingDate(): LocalDate {
        val today = LocalDate.now()
        val closingDay = invoiceClosingDayOfMonth
        val lastDayOfMonth = getLastDayOfMonth(today)

        if (closingDay > today.dayOfMonth) {
            val previousMonthDate = today.withDayOfMonth(1).minusMonths(1)
            val lastDayOfPreviousMonth = getLastDayOfMonth(previousMonthDate)
            if (closingDay > lastDayOfPreviousMonth) {
                return previousMonthDate.withDayOfMonth(lastDayOfPreviousMonth)
            }
            return previousMonthDate.withDayOfMonth(closingDay)
        }

        if (closingDay > lastDayOfMonth) {
            return today.withDayOfMonth(lastDayOfMonth)
        }
        return today.withDayOfMonth(closingDay)
    }

    private fun getCurrentClosingDate(): LocalDate {
        val today = LocalDate.now()
        val closingDay = invoiceClosingDayOfMonth
        val lastDayOfMonth = getLastDayOfMonth(today)

        if (closingDay > today.dayOfMonth) {
            if (closingDay > lastDayOfMonth) {
                return today.withDayOfMonth(lastDayOfMonth)
            }
            return today.withDayOfMonth(closingDay)
        }

        val nextMonthDate = today.withDayOfMonth(1).plusMonths(1)
        val lastDayOfNextMonth = getLastDayOfMonth(nextMonthDate)
        if (closingDay > lastDayOfNextMonth) {
            return nextMonthDate.withDayOfMonth(lastDayOfNextMonth)
        }
        return nextMonthDate.withDayOfMonth(closingDay)
    }

    private fun getLastDayOfMonth(date: LocalDate) = date.month.length(date.isLeapYear)
}
