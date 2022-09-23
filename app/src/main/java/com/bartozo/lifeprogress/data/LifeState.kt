package com.bartozo.lifeprogress.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface LifeState {
    @Serializable
    object Loading : LifeState

    @Serializable
    data class Available(
        val age: Int,
        val weekOfYear: Int,
        val lifeExpectancy: Int
    ) : LifeState

    @Serializable
    data class Unavailable(val message: String) : LifeState
}