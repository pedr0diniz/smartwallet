package br.com.zinid.smartwallet.domain.utils

import java.time.LocalDate

object DateHelper {

    fun getClosingDateWithValidDay(date: LocalDate, possibleClosingDay: Int): LocalDate {
        val lastDayOfMonth = date.month.length(date.isLeapYear)
        if (possibleClosingDay > lastDayOfMonth) {
            return date.withDayOfMonth(lastDayOfMonth)
        }
        return date.withDayOfMonth(possibleClosingDay)
    }

    fun LocalDate.isBeforeOrEqual(date: LocalDate): Boolean =
        this.isBefore(date) || this.isEqual(date)

    fun LocalDate.isAfterOrEqual(date: LocalDate): Boolean =
        this.isAfter(date) || this.isEqual(date)
}
