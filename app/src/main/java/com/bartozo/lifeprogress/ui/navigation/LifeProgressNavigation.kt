package com.bartozo.lifeprogress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bartozo.lifeprogress.ui.screens.*
import com.bartozo.lifeprogress.ui.viewmodels.AboutViewModel
import com.bartozo.lifeprogress.ui.viewmodels.HomeViewModel
import com.bartozo.lifeprogress.ui.viewmodels.OnboardingViewModel
import com.bartozo.lifeprogress.ui.viewmodels.ProfileViewModel

sealed class Screen(val route: String) {
    object Onboarding: Screen("onboarding")
    object Home: Screen("home")
    object Profile: Screen("profile")
    object About: Screen("about")
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LifeProgressNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Onboarding,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Screen.Onboarding.route) {
            val viewModel = hiltViewModel<OnboardingViewModel>()
            OnboardingScreen(
                viewModel = viewModel,
                navigateToHomeScreen = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                navigateToProfileScreen = { navController.navigate(Screen.Profile.route) },
                navigateToAboutScreen = { navController.navigate(Screen.About.route) }
            )
        }
        composable(Screen.Profile.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel,
                navigateBackToHomeScreen = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Profile.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.About.route) {
            val viewModel = hiltViewModel<AboutViewModel>()
            AboutScreen(
                viewModel = viewModel,
                navigateBackToHomeScreen = {
                    navController.popBackStack(
                        route = Screen.Home.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}
