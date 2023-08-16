package br.com.zinid.smartwallet.domain.utils

import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

internal class DateHelperTest {

    @Test
    fun `should return date with valid day for february`() {
        val date = DateHelper.getDateWithValidDay(LocalDate.parse("2023-02-02"), 31)
        assertEquals(28, date.lengthOfMonth())
    }

    @Test
    fun `should return date with valid day for any other month`() {
        val date = DateHelper.getDateWithValidDay(LocalDate.parse("2023-01-01"), 31)
        assertEquals(31, date.lengthOfMonth())
    }
}
