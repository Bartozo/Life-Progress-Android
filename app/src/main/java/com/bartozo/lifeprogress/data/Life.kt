package com.bartozo.lifeprogress.data

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.abs

data class Life(
    val age: Int,
    val weekOfYear: Int,
    val lifeExpectancy: Int
) {
    companion object {
        const val totalWeeksInAYear = 52

        operator fun invoke(birthday: LocalDate, lifeExpectancy: Int): Life {
            val todayDate: LocalDate = LocalDate.now()
            val age = ChronoUnit.YEARS.between(birthday, todayDate)
            val weekOfYear = ChronoUnit.WEEKS.between(
                LocalDate.of(todayDate.year, birthday.month, birthday.dayOfMonth),
                todayDate
            ).toInt()

            return Life(
                age = age.toInt(),
                weekOfYear = if (weekOfYear < 0) {
                    totalWeeksInAYear - abs(weekOfYear)
                } else {
                    weekOfYear
                },
                lifeExpectancy = lifeExpectancy
            )
        }

        private fun getDefaultBirthDay(): LocalDate {
            val defaultUserAge = 22
            return LocalDate.now()
                .minusYears(defaultUserAge.toLong())
        }

        // this was used for swiftui previews
         val example: Life by lazy {
             Life(
                birthday = getDefaultBirthDay(),
                lifeExpectancy = 72
            )
        }
    }

    val progress: Double by lazy {
        val realAge = age.toDouble() + weekOfYear.toDouble() / totalWeeksInAYear.toDouble()
        val progress = realAge / lifeExpectancy.toDouble()

        progress
    }

    val formattedProgress: String by lazy {
        "${"%.2f".format(progress * 100)}%"
    }

    val currentYearProgress: Double by lazy {
        weekOfYear.toDouble() / totalWeeksInAYear.toDouble()
    }

    val formattedCurrentYearProgress: String by lazy {
        "${"%.2f".format(currentYearProgress * 100)}%"
    }

    val currentYearRemainingWeeks: Int by lazy {
        totalWeeksInAYear - weekOfYear
    }

    val currentYearSpentWeeks: Int by lazy {
        totalWeeksInAYear - currentYearRemainingWeeks
    }

    val numberOfWeeksSpent: Int by lazy {
        totalWeeksInAYear * age + weekOfYear
    }

    val numberOfWeeksLeft: Int by lazy {
        totalWeeksInAYear * lifeExpectancy - numberOfWeeksSpent
    }
}
