package com.bartozo.lifeprogress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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
fun LifeProgressNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Welcome,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Screen.Welcome.route) {
            val viewModel = hiltViewModel<WelcomeViewModel>()
            WelcomeScreen(
                viewModel = viewModel,
                navigateToHomeScreen = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
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
                navigateBackToHomeScreen = {
                    navController.popBackStack(
                        route = Screen.Home.route,
                        inclusive = false
                    )
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
