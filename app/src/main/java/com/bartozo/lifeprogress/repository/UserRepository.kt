package com.bartozo.lifeprogress.repository

import com.bartozo.lifeprogress.data.AppTheme
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface UserRepository {
    val birthDay: Flow<LocalDate>
    val lifeExpectancy: Flow<Int>
    val didSeeOnboarding: Flow<Boolean>
    val appTheme: Flow<AppTheme>
    val isWeeklyNotificationEnabled: Flow<Boolean>

    suspend fun updateBirthDay(birthDay: LocalDate)
    suspend fun updateLifeExpectancy(lifeExpectancy: Int)

    suspend fun updateDidSeeOnboarding(didSeeOnboarding: Boolean)
    suspend fun updateAppTheme(appTheme: AppTheme)
    suspend fun updateIsWeeklyNotificationEnabled(isEnabled: Boolean)
}