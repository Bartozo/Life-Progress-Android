package com.bartozo.lifeprogress.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface UserRepository {
    val birthDay: Flow<LocalDate>
    val lifeExpectancy: Flow<Int>
    val didSeeWelcome: Flow<Boolean>

    suspend fun updateBirthDay(birthDay: LocalDate)
    suspend fun updateLifeExpectancy(lifeExpectancy: Int)
    suspend fun updateDidSeeWelcome(didSeeWelcome: Boolean)
}