package com.bartozo.lifeprogress.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import com.bartozo.lifeprogress.ui.components.welcome.Feature
import com.bartozo.lifeprogress.ui.components.welcome.PrimaryActionColors
import com.bartozo.lifeprogress.ui.components.welcome.Welcome


@OptIn(ExperimentalTextApi::class)
@Composable
fun WelcomeScreen(
    navigateToProfileScreen: () -> Unit,
) {
    Welcome(
        title = "Welcome to",
        titleTextStyle = MaterialTheme.typography.displaySmall,
        secondaryTitle = "Life Progress",
        secondaryTextStyle = MaterialTheme.typography.displaySmall.copy(
            brush  = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF069DDE),
                    Color(0xFF5FB945),
                    Color(0xFFF9B828),
                    Color(0xFFF7801A),
                    Color(0xFFDF3B3C),
                    Color(0xFF943B95),
                    Color(0xFF955F3B)
                )
            ),
        ),
        features = listOf(
            Feature(
                title = "Measure your Life Progress",
                subtitle = "See the progress of your life and current year with easy to use calendar.",
                image = Icons.Outlined.DateRange
            ),
            Feature(
                title = "Widget",
                subtitle = "Always be up to date with your life progress with super handy " +
                        "and useful widget on your home screen.",
                image = Icons.Outlined.Widgets
            ),
            Feature(
                title = "App Theme",
                subtitle = "Material You (The application color palette is generated based on " +
                        "the user's wallpaper, only available for android 12 or higher).",
                image = Icons.Outlined.Palette
            ),
            Feature(
                title = "Different Modes",
                subtitle = "Works in both Horizontal and Vertical mode.",
                image = Icons.Outlined.Devices
            ),
        ),
        primaryActionColors = PrimaryActionColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onPrimaryActionClicked = navigateToProfileScreen,
    )
}
