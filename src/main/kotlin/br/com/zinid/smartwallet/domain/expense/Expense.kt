package br.com.zinid.smartwallet.domain.expense

import br.com.zinid.smartwallet.domain.exception.InvalidDateRangeException
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.utils.DateHelper.isAfterOrEqual
import br.com.zinid.smartwallet.domain.utils.DateHelper.isBeforeOrEqual
import java.math.BigDecimal
import java.time.LocalDate

interface Expense {
    val id: Long?
    val content: String
    val date: LocalDate
    val price: BigDecimal
    val essential: Boolean?
    val monthlySubscription: Boolean?
    val paymentMethod: PaymentMethod

    fun wasPurchasedWithinDateRange(startDate: LocalDate, endDate: LocalDate): Boolean {
        if (startDate.isAfter(endDate)) {
            throw InvalidDateRangeException(INVALID_DATE_RANGE_MESSAGE.format(startDate, endDate))
        }

        return (date.isAfterOrEqual(startDate)) && (date.isBeforeOrEqual(endDate))
    }

    fun getPaymentType(): PaymentType

    fun canBeMade() = paymentMethod.canPurchase(this)

    fun process()

    companion object {
        private const val INVALID_DATE_RANGE_MESSAGE = "Date range is invalid with start date [%s] and end date [%s]"
    }
}
