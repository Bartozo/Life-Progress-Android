package com.bartozo.lifeprogress.util

import java.time.LocalDate

/**
 * Calculates the range of years between today and given [years].
 */
fun rangeOfYearsFromNowTo(years: Int): IntRange {
    val today = LocalDate.now()
    val currentYear = today.year
    val startingYear = currentYear - years

    return startingYear..currentYear
}