package com.bartozo.lifeprogress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bartozo.lifeprogress.ui.screens.AboutScreen
import com.bartozo.lifeprogress.ui.screens.HomeScreen
import com.bartozo.lifeprogress.ui.screens.ProfileScreen
import com.bartozo.lifeprogress.ui.screens.WelcomeScreen
import com.bartozo.lifeprogress.ui.viewmodels.AboutViewModel
import com.bartozo.lifeprogress.ui.viewmodels.HomeViewModel
import com.bartozo.lifeprogress.ui.viewmodels.ProfileViewModel
import com.bartozo.lifeprogress.ui.viewmodels.WelcomeViewModel

sealed class Screen(val route: String) {
    object Welcome: Screen("welcome")
    object Home: Screen("home")
    object Profile: Screen("profile")
    object About: Screen("about")
}

@Composable
fun LifeProgressNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            val viewModel = hiltViewModel<WelcomeViewModel>()
            WelcomeScreen(
                viewModel = viewModel,
                navigateToHomeScreen = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                navigateToProfileScreen = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
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
                navigateBackToHomeScreen = {}
            )
        }
        composable(Screen.About.route) {
            val viewModel = hiltViewModel<AboutViewModel>()
            AboutScreen(
                viewModel = viewModel,
                onBackClick = {}
            )
        }
    }
}