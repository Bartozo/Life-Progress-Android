package com.bartozo.lifeprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.bartozo.lifeprogress.ui.navigation.LifeProgressNavigation
import com.bartozo.lifeprogress.ui.navigation.Screen
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.MainEventState
import com.bartozo.lifeprogress.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.mainUiState.value == MainEventState.Loading
            }
        }
        super.onCreate(savedInstanceState)

        setContent {
            LifeProgressTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mainUiState: MainEventState by viewModel.mainUiState.collectAsState(
                        initial = MainEventState.Loading
                    )
                    val startDestination = when (mainUiState) {
                        MainEventState.Loading -> null
                        MainEventState.NavigateToHomeScreen -> Screen.Home
                        MainEventState.NavigateToWelcomeScreen -> Screen.Welcome
                    }

                    if (startDestination != null) {
                        LifeProgressNavigation(startDestination = startDestination)
                    }
                }
            }
        }
    }
}
