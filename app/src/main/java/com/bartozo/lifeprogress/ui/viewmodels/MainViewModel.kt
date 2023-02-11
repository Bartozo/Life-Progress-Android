package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class MainEventState {
    object Loading : MainEventState()
    object NavigateToHomeScreen : MainEventState()
    object NavigateToWelcomeScreen : MainEventState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _mainUiState = MutableStateFlow<MainEventState>(MainEventState.Loading)
    val mainUiState: StateFlow<MainEventState> = _mainUiState.asStateFlow()

    val appTheme = userRepository.appTheme
    init {
        viewModelScope.launch {
            if (userRepository.didSeeWelcome.first()) {
                _mainUiState.value = MainEventState.NavigateToHomeScreen
            } else {
                _mainUiState.value = MainEventState.NavigateToWelcomeScreen
            }
        }
    }
}