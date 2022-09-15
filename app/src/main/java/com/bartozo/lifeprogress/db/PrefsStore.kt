package com.bartozo.lifeprogress.db

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PrefsStore {
    fun birthDayFlow(): Flow<LocalDate>
    fun lifeExpectancyFlow(): Flow<Int>
    fun didSeeWelcomeFlow(): Flow<Boolean>

    suspend fun saveBirthDay(birthDay: LocalDate)
    suspend fun saveLifeExpectancy(lifeExpectancy: Int)
    suspend fun updatedDidSeeWelcome(didSeeWelcome: Boolean)
}