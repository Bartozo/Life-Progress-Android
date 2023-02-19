package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


sealed class OnboardingEventState {
    object Idle : OnboardingEventState()
    object NavigateToHomeScreen : OnboardingEventState()
}

sealed class OnboardingUiState {
    object WelcomeState: OnboardingUiState()
    object BirthdayState : OnboardingUiState()
    object LifeExpectancyState : OnboardingUiState()
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _eventState = MutableStateFlow<OnboardingEventState>(OnboardingEventState.Idle)
    val eventState: StateFlow<OnboardingEventState> = _eventState.asStateFlow()

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.WelcomeState)
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _birthDay = MutableStateFlow<LocalDate?>(value = null)
    val birthDay: StateFlow<LocalDate?> = _birthDay

    private val _lifeExpectancy = MutableStateFlow<Int?>(value = null)
    val lifeExpectancy: StateFlow<Int?> = _lifeExpectancy

    fun updateBirthDay(birthDay: LocalDate) = viewModelScope.launch {
        userRepository.updateBirthDay(birthDay = birthDay)
        _birthDay.emit(birthDay)
    }

    fun updateLifeExpectancy(lifeExpectancy: Int) = viewModelScope.launch {
        userRepository.updateLifeExpectancy(lifeExpectancy = lifeExpectancy)
        _lifeExpectancy.emit(lifeExpectancy)
    }

    fun moveToBirthday() = viewModelScope.launch {
        _uiState.emit(OnboardingUiState.BirthdayState)
    }

    fun moveToLifeExpectancy() = viewModelScope.launch {
        _uiState.emit(OnboardingUiState.LifeExpectancyState)
    }

    fun navigateToHome() = viewModelScope.launch {
        userRepository.updateDidSeeOnboarding(true)
        _eventState.emit(OnboardingEventState.NavigateToHomeScreen)
    }
}