package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartozo.lifeprogress.db.PrefsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefsStore: PrefsStore
) : ViewModel() {

    val birthDay: StateFlow<LocalDate?>
        get() = prefsStore.birthDayFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, LocalDate.now())

    val lifeExpectancy: StateFlow<Int?>
        get() = prefsStore.lifeExpectancyFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, 30)

    fun updateBirthDay(birthDay: LocalDate) = viewModelScope.launch {
        prefsStore.saveBirthDay(birthDay = birthDay)
    }

    fun updateLifeExpectancy(lifeExpectancy: Int) = viewModelScope.launch {
        prefsStore.saveLifeExpectancy(lifeExpectancy = lifeExpectancy)
    }
}