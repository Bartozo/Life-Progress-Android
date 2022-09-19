package com.bartozo.lifeprogress.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val lifeFlow = combine(
        userRepository.birthDay,
        userRepository.lifeExpectancy
    ) { birthDay: LocalDate, lifeExpectancy: Int ->
        return@combine Life.invoke(
            birthday = birthDay,
            lifeExpectancy = lifeExpectancy
        )
    }
}