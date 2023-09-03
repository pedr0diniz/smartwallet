package br.com.zinid.smartwallet.domain.utils

import java.time.LocalDate

object DateHelper {

    private val today = LocalDate.now()

    fun getDateWithValidDay(date: LocalDate, possibleDay: Int): LocalDate {
        val lastDayOfMonth = date.month.length(date.isLeapYear)
        if (possibleDay > lastDayOfMonth) {
            return date.withDayOfMonth(lastDayOfMonth)
        }
        return date.withDayOfMonth(possibleDay)
    }

    fun getPreviousDueDate(invoiceDueDayOfMonth: Int, givenDate: LocalDate?): LocalDate {
        val referenceDate = givenDate ?: today

        return when (invoiceDueDayOfMonth > referenceDate.dayOfMonth) {
            true -> getDateWithValidDay(referenceDate.minusMonths(1), invoiceDueDayOfMonth)
            false -> getDateWithValidDay(referenceDate, invoiceDueDayOfMonth)
        }
    }

    fun getCurrentDueDate(invoiceDueDayOfMonth: Int, givenDate: LocalDate?): LocalDate {
        val referenceDate = givenDate ?: today

        return when (invoiceDueDayOfMonth > referenceDate.dayOfMonth) {
            true -> getDateWithValidDay(referenceDate, invoiceDueDayOfMonth)
            false -> getDateWithValidDay(referenceDate.plusMonths(1), invoiceDueDayOfMonth)
        }
    }

    fun LocalDate.isBeforeOrEqual(date: LocalDate): Boolean =
        this.isBefore(date) || this.isEqual(date)

    fun LocalDate.isAfterOrEqual(date: LocalDate): Boolean =
        this.isAfter(date) || this.isEqual(date)
}
