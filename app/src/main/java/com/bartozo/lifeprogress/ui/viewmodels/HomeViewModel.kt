package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.db.PrefsStore
import com.bartozo.lifeprogress.ui.components.LifeCalendarDisplayMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prefsStore: PrefsStore
) : ViewModel() {

    val lifeFlow = combine(
        prefsStore.birthDayFlow(),
        prefsStore.lifeExpectancyFlow()
    ) { birthDay: LocalDate, lifeExpectancy: Int ->
        return@combine Life.invoke(
            birthday = birthDay,
            lifeExpectancy = lifeExpectancy
        )
    }

    private val _displayModeState = MutableStateFlow(value = LifeCalendarDisplayMode.LIFE)
    val displayModeState: StateFlow<LifeCalendarDisplayMode> = _displayModeState

    fun updateDisplayMode(displayMode: LifeCalendarDisplayMode) {
        _displayModeState.value = displayMode
    }
}