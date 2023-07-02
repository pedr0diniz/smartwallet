package br.com.zinid.smartwallet.domain.utils

import java.time.LocalDate

object DateHelper {

    fun getDateWithValidDay(date: LocalDate, possibleDay: Int): LocalDate {
        val lastDayOfMonth = date.month.length(date.isLeapYear)
        if (possibleDay > lastDayOfMonth) {
            return date.withDayOfMonth(lastDayOfMonth)
        }
        return date.withDayOfMonth(possibleDay)
    }

    fun LocalDate.isBeforeOrEqual(date: LocalDate): Boolean =
        this.isBefore(date) || this.isEqual(date)

    fun LocalDate.isAfterOrEqual(date: LocalDate): Boolean =
        this.isAfter(date) || this.isEqual(date)
}
