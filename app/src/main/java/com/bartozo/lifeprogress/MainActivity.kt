package com.bartozo.lifeprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.bartozo.lifeprogress.data.AppTheme
import com.bartozo.lifeprogress.ui.navigation.LifeProgressNavigation
import com.bartozo.lifeprogress.ui.navigation.Screen
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.MainEventState
import com.bartozo.lifeprogress.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*
    companion object {
        private const val APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.IMMEDIATE
        private const val REQUEST_UPDATE = 100
    }
    */

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // checkForUpdates()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.mainUiState.value == MainEventState.Loading
            }
        }
        super.onCreate(savedInstanceState)

        setContent {
            val appTheme = viewModel.appTheme.collectAsState(initial = AppTheme.SYSTEM_AUTO)
            val isDarkTheme = when (appTheme.value) {
                AppTheme.SYSTEM_AUTO -> isSystemInDarkTheme()
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }

            LifeProgressTheme(darkTheme = isDarkTheme) {
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
                        MainEventState.NavigateToOnboardingScreen -> Screen.Onboarding
                    }

                    if (startDestination != null) {
                        LifeProgressNavigation(startDestination = startDestination)
                    }
                }
            }
        }
    }

    /*
    private fun checkForUpdates() {
        val appUpdateManager : AppUpdateManager
        if (BuildConfig.DEBUG) {
            appUpdateManager = FakeAppUpdateManager(baseContext)
            appUpdateManager.setUpdateAvailable(2)
        } else {
            appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        }

        val appUpdateInfo = appUpdateManager.appUpdateInfo

        appUpdateInfo.addOnSuccessListener {
            handleUpdate(appUpdateManager, appUpdateInfo)
        }
    }

    private fun handleUpdate(manager: AppUpdateManager, info: Task<AppUpdateInfo>) {
        if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.IMMEDIATE) {
            handleImmediateUpdate(manager, info)
        }
    }

    private fun handleImmediateUpdate(manager: AppUpdateManager, info: Task<AppUpdateInfo>) {
        if ((info.result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
            info.result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
            && info.result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

            manager.startUpdateFlowForResult(info.result,
                AppUpdateType.IMMEDIATE, this, REQUEST_UPDATE)
        }

        // Simulates an immediate update
        if (BuildConfig.DEBUG) {
            val fakeAppUpdate = manager as FakeAppUpdateManager

            if (fakeAppUpdate.isImmediateFlowVisible) {
                fakeAppUpdate.userAcceptsUpdate()
                fakeAppUpdate.downloadStarts()
                fakeAppUpdate.downloadCompletes()
                launchRestartDialog(manager)
            }
        }
    }

    private fun launchRestartDialog(manager: AppUpdateManager) {
        AlertDialog.Builder(this)
            .setTitle("App update")
            .setMessage("Application successfully updated! You need to restart the app in " +
                    "order to use this new features.")
            .setPositiveButton("Restart") { _, _ ->
                manager.completeUpdate()
            }
            .create().show()
    }
    */
}
