package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.db.PrefsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
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
}