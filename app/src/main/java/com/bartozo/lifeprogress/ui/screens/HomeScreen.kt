package com.bartozo.lifeprogress.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.components.*
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.HomeViewModel
import com.bartozo.lifeprogress.util.sendMail

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToProfileScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit
) {
    val life: Life by viewModel.lifeFlow
        .collectAsState(initial = Life.example)
    val displayMode: LifeCalendarDisplayMode by viewModel.displayModeState
        .collectAsState()
    val title = when (displayMode) {
        LifeCalendarDisplayMode.CURRENT_YEAR -> stringResource(id = R.string.year_progress, life.formattedCurrentYearProgress)
        LifeCalendarDisplayMode.LIFE -> stringResource(id = R.string.life_progress, life.formattedProgress)
    }
    val subtitle = when (displayMode) {
        LifeCalendarDisplayMode.CURRENT_YEAR -> pluralStringResource(
            id = R.plurals.weeks_until_birthday,
            count = life.currentYearRemainingWeeks,
            life.currentYearRemainingWeeks
        )
        LifeCalendarDisplayMode.LIFE ->
            pluralStringResource(
                id = R.plurals.weeks_spent,
                count = life.numberOfWeeksSpent,
                life.numberOfWeeksSpent
            ) +
            " • " +
            pluralStringResource(
                id = R.plurals.weeks_left,
                count = life.numberOfWeeksLeft,
                life.numberOfWeeksLeft
            )
    }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            HomeTopBar(
                title = title,
                subtitle = subtitle,
                navigateToProfileScreen = navigateToProfileScreen,
                navigateToAboutScreen = navigateToAboutScreen,
                onShareFeedbackPressed = {
                    context.sendMail(
                        to = "bartozo.dev@gmail.com",
                        subject = "Life Progress - Feedback",
                        onError = {}
                    )
                },
                onRateAppPressed = {
                    uriHandler.openUri("https://play.google.com/store/apps/details?id=com.bartozo.lifeprogress")
                }
            )
        },
        content = { innerPaddings ->
            CanvasLifeCalendar(
                modifier = Modifier
                    .padding(innerPaddings)
                    .clickable {
                        val newDisplayMode = if (displayMode == LifeCalendarDisplayMode.LIFE) {
                            LifeCalendarDisplayMode.CURRENT_YEAR
                        } else {
                            LifeCalendarDisplayMode.LIFE
                        }
                        viewModel.updateDisplayMode(displayMode = newDisplayMode)
                    }
                    .padding(16.dp),
                life = life,
                displayMode = displayMode
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    navigateToProfileScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    onShareFeedbackPressed: () -> Unit,
    onRateAppPressed: () -> Unit,
) {
    Column {
        LargeTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                )
            },
            actions = {
                NavigationDropDownMenu(
                    navigateToProfileScreen = navigateToProfileScreen,
                    navigateToAboutScreen = navigateToAboutScreen,
                    onShareFeedbackPressed = onShareFeedbackPressed,
                    onRateAppPressed = onRateAppPressed
                )
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp),
            text = subtitle,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NavigationDropDownMenu(
    modifier: Modifier = Modifier,
    navigateToProfileScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit,
    onShareFeedbackPressed: () -> Unit,
    onRateAppPressed: () -> Unit,
) {
    val isExpanded = remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { isExpanded.value = true }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = stringResource(id = R.string.navigation)
            )
        }
        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.profile_button_text))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Person Icon"
                    )
                },
                onClick = {
                    isExpanded.value = false
                    navigateToProfileScreen.invoke()
                },
            )
            Divider()
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.about_this_app_button_text))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info Icon"
                    )
                },
                onClick = {
                    isExpanded.value = false
                    navigateToAboutScreen.invoke()
                },
            )
            Divider()
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.share_feedback_button_text))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Mail,
                        contentDescription = "Mail Icon"
                    )
                },
                onClick = {
                    isExpanded.value = false
                    onShareFeedbackPressed.invoke()
                },
            )
            Divider()
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.rate_app_button_text))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = "Star Icon"
                    )
                },
                onClick = {
                    isExpanded.value = false
                    onRateAppPressed.invoke()
                },
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
private fun HomeTopBarPreview() {
    LifeProgressTheme {
        HomeTopBar(
            title = stringResource(id = R.string.year_progress, Life.example.formattedCurrentYearProgress),
            subtitle = pluralStringResource(
                id = R.plurals.weeks_until_birthday,
                count = Life.example.currentYearRemainingWeeks,
                Life.example.currentYearRemainingWeeks
            ),
            navigateToProfileScreen = {},
            navigateToAboutScreen = {},
            onShareFeedbackPressed = {},
            onRateAppPressed = {}
        )
    }
}

@Preview
@Composable
fun NavigationDropDownMenuPreview() {
    LifeProgressTheme {
        NavigationDropDownMenu(
            navigateToAboutScreen = {},
            navigateToProfileScreen = {},
            onShareFeedbackPressed = {},
            onRateAppPressed = {}
        )
    }
}