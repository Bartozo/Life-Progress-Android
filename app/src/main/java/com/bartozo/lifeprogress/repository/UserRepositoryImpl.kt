package com.bartozo.lifeprogress.repository

import com.bartozo.lifeprogress.db.PrefsStore
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefsStore: PrefsStore
) : UserRepository {
    override val birthDay: Flow<LocalDate> = prefsStore.birthDayFlow()
    override val lifeExpectancy: Flow<Int> = prefsStore.lifeExpectancyFlow()
    override val didSeeWelcome: Flow<Boolean> = prefsStore.didSeeWelcomeFlow()

    override suspend fun updateBirthDay(birthDay: LocalDate) {
        prefsStore.saveBirthDay(birthDay)
    }

    override suspend fun updateLifeExpectancy(lifeExpectancy: Int) {
        prefsStore.saveLifeExpectancy(lifeExpectancy)
    }

    override suspend fun updateDidSeeWelcome(didSeeWelcome: Boolean) {
        prefsStore.updatedDidSeeWelcome(didSeeWelcome)
    }
}