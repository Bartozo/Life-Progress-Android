package com.bartozo.lifeprogress.repository

import com.bartozo.lifeprogress.data.AppTheme
import com.bartozo.lifeprogress.db.PrefsStore
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefsStore: PrefsStore
) : UserRepository {
    override val birthDay: Flow<LocalDate> = prefsStore.birthDayFlow()
    override val lifeExpectancy: Flow<Int> = prefsStore.lifeExpectancyFlow()
    override val didSeeOnboarding: Flow<Boolean> = prefsStore.didSeeOnboardingFlow()
    override val appTheme: Flow<AppTheme> = prefsStore.appThemeFlow()
    override val isWeeklyNotificationEnabled: Flow<Boolean> = prefsStore
        .isWeeklyNotificationEnabledFlow()

    override suspend fun updateBirthDay(birthDay: LocalDate) {
        prefsStore.saveBirthDay(birthDay)
    }

    override suspend fun updateLifeExpectancy(lifeExpectancy: Int) {
        prefsStore.saveLifeExpectancy(lifeExpectancy)
    }

    override suspend fun updateDidSeeOnboarding(didSeeOnboarding: Boolean) {
        prefsStore.updateDidSeeOnboarding(didSeeOnboarding)
    }

    override suspend fun updateAppTheme(appTheme: AppTheme) {
        prefsStore.updateAppTheme(appTheme)
    }

    override suspend fun updateIsWeeklyNotificationEnabled(isEnabled: Boolean) {
        prefsStore.updateIsWeeklyNotificationEnabled(isEnabled)
    }
}