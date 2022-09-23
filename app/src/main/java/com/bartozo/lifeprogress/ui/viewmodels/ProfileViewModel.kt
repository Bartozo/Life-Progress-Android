package com.bartozo.lifeprogress.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.repository.UserRepository
import com.bartozo.lifeprogress.ui.appwidgets.LifeProgressWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val birthDay: StateFlow<LocalDate?>
        get() = userRepository.birthDay
            .stateIn(viewModelScope, SharingStarted.Lazily, LocalDate.now())

    val lifeExpectancy: StateFlow<Int?>
        get() = userRepository.lifeExpectancy
            .stateIn(viewModelScope, SharingStarted.Lazily, 30)

    fun updateBirthDay(birthDay: LocalDate, context: Context) = viewModelScope.launch {
        userRepository.updateBirthDay(birthDay = birthDay)
        updateAppWidget(context = context)
    }

    fun updateLifeExpectancy(lifeExpectancy: Int, context: Context) = viewModelScope.launch {
        userRepository.updateLifeExpectancy(lifeExpectancy = lifeExpectancy)
        updateAppWidget(context = context)
    }

    private fun updateAppWidget(context: Context) {
        LifeProgressWorker.enqueue(context, true)
    }
}