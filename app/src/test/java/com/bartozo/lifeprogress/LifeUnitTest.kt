package com.bartozo.lifeprogress

import com.bartozo.lifeprogress.data.Life
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class LifeUnitTest {

    private lateinit var life: Life

    companion object {
        private const val year = 1999
        private const val month = 11
        private const val dayOfMonth = 12
        private const val lifeExpectancy = 90
        private val todayDate = LocalDate.of(2022, 10, 15)
    }

    @Before
    fun initSetUp() {
        this.life = Life(
            birthday = LocalDate.of(year, month, dayOfMonth),
            lifeExpectancy = lifeExpectancy
        )
    }

    @Test
    fun lifeExpectancy_isCorrect() {
        Assert.assertEquals(lifeExpectancy, life.lifeExpectancy)
    }

    @Test
    fun age_isCorrect() {
        val age = ChronoUnit.YEARS.between(LocalDate.of(year, month, dayOfMonth), todayDate).toInt()

        Assert.assertEquals(age, life.age)
    }

    @Test
    fun currentYearProgress_isCorrect() {
        val currentYearProgress = 0.923

        Assert.assertEquals(currentYearProgress, life.currentYearProgress, 0.001)
    }

    @Test
    fun formattedCurrentYearProgress_isCorrect() {
        val formattedCurrentYearProgress = "92.31%"

        Assert.assertEquals(formattedCurrentYearProgress, life.formattedCurrentYearProgress)
    }

    @Test
    fun progress_isCorrect() {
        val progress = 0.254

        Assert.assertEquals(progress, life.progress, 0.001)
    }

    @Test
    fun formattedProgress_isCorrect() {
        val formattedProgress = "25.47%"

        Assert.assertEquals(formattedProgress, life.formattedProgress)
    }

    @Test
    fun numberOfWeeksSpent_isCorrect() {
        val numberOfWeeksSpent = 1192

        Assert.assertEquals(numberOfWeeksSpent, life.numberOfWeeksSpent)
    }

    @Test
    fun numberOfWeeksLeft_isCorrect() {
        val numberOfWeeksLeft = 3488

        Assert.assertEquals(numberOfWeeksLeft, life.numberOfWeeksLeft)
    }
}