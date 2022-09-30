package com.bartozo.lifeprogress.db

import com.bartozo.lifeprogress.data.AppTheme
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PrefsStore {
    fun birthDayFlow(): Flow<LocalDate>
    fun lifeExpectancyFlow(): Flow<Int>
    fun didSeeWelcomeFlow(): Flow<Boolean>
    fun appThemeFlow(): Flow<AppTheme>

    suspend fun saveBirthDay(birthDay: LocalDate)
    suspend fun saveLifeExpectancy(lifeExpectancy: Int)
    suspend fun updatedDidSeeWelcome(didSeeWelcome: Boolean)
    suspend fun updateAppTheme(appTheme: AppTheme)
}