package com.bartozo.lifeprogress.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.data.AppTheme
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.repository.UserRepository
import com.bartozo.lifeprogress.ui.appwidgets.LifeProgressWorker
import com.bartozo.lifeprogress.worker.WeeklyNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val birthDay: StateFlow<LocalDate>
        get() = userRepository.birthDay
            .stateIn(viewModelScope, SharingStarted.Lazily, LocalDate.now())

    val lifeExpectancy: StateFlow<Int>
        get() = userRepository.lifeExpectancy
            .stateIn(viewModelScope, SharingStarted.Lazily, 30)

    val appTheme: StateFlow<AppTheme>
        get() = userRepository.appTheme
            .stateIn(viewModelScope, SharingStarted.Lazily, AppTheme.SYSTEM_AUTO)

    val isWeeklyNotificationEnabled: StateFlow<Boolean>
        get() = userRepository.isWeeklyNotificationEnabled
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val lifeFlow = combine(
        userRepository.birthDay,
        userRepository.lifeExpectancy
    ) { birthDay: LocalDate, lifeExpectancy: Int ->
        return@combine Life.invoke(
            birthday = birthDay,
            lifeExpectancy = lifeExpectancy
        )
    }

    fun updateBirthDay(birthDay: LocalDate, context: Context) = viewModelScope.launch {
        userRepository.updateBirthDay(birthDay = birthDay)
        updateAppWidget(context = context)
    }

    fun updateLifeExpectancy(lifeExpectancy: Int, context: Context) = viewModelScope.launch {
        userRepository.updateLifeExpectancy(lifeExpectancy = lifeExpectancy)
        updateAppWidget(context = context)
    }

    fun updateAppTheme(appTheme: AppTheme) = viewModelScope.launch {
        userRepository.updateAppTheme(appTheme = appTheme)
    }

    fun updateIsWeeklyNotificationEnabled(isEnabled: Boolean, context: Context) = viewModelScope.launch {
        userRepository.updateIsWeeklyNotificationEnabled(isEnabled = isEnabled)
        updateWeeklyNotificationWorker(isEnabled = isEnabled, context = context)
    }

    private fun updateAppWidget(context: Context) {
        LifeProgressWorker.enqueue(context = context, force = true)
    }

    private fun updateWeeklyNotificationWorker(isEnabled: Boolean, context: Context) {
        if (isEnabled) {
            WeeklyNotificationWorker.enqueue(context = context, force = true)
        } else {
            WeeklyNotificationWorker.cancel(context = context)
        }
    }
}