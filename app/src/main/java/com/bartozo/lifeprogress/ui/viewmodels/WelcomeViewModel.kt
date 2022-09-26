package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WelcomeEventState {
    object Idle : WelcomeEventState()
    object NavigateToProfileScreen : WelcomeEventState()
}

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _welcomeEvent = MutableStateFlow<WelcomeEventState>(value = WelcomeEventState.Idle)
    val welcomeEvent: StateFlow<WelcomeEventState> = _welcomeEvent

    fun navigateToProfileScreen() = viewModelScope.launch {
        userRepository.updateDidSeeWelcome(true)
        _welcomeEvent.value = WelcomeEventState.NavigateToProfileScreen
    }
 }