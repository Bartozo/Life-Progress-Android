package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.db.PrefsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WelcomeEventState {
    object Idle : WelcomeEventState()
    object NavigateToProfileScreen : WelcomeEventState()
    object OnDidSeeWelcomeScreen : WelcomeEventState()
}

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val prefsStore: PrefsStore
) : ViewModel() {

    private val _welcomeEvent = MutableStateFlow<WelcomeEventState>(value = WelcomeEventState.Idle)
    val welcomeEvent: StateFlow<WelcomeEventState> = _welcomeEvent

    init {
        viewModelScope.launch {
            if (prefsStore.didSeeWelcomeFlow().first()) {
                _welcomeEvent.value = WelcomeEventState.OnDidSeeWelcomeScreen
            }
        }
    }

    fun navigateToProfileScreen() = viewModelScope.launch {
        prefsStore.updatedDidSeeWelcome(true)
        _welcomeEvent.value = WelcomeEventState.NavigateToProfileScreen
    }
 }