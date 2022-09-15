package com.bartozo.lifeprogress.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.components.*
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
        LifeCalendarDisplayMode.CURRENT_YEAR -> "Year Progress: ${life.formattedCurrentYearProgress}"
        LifeCalendarDisplayMode.LIFE -> "Life Progress: ${life.formattedProgress}"
    }
    val subtitle = when (displayMode) {
        LifeCalendarDisplayMode.CURRENT_YEAR -> "${(life.currentYearRemainingWeeks)} weeks until your birthday"
        LifeCalendarDisplayMode.LIFE -> "${(life.numberOfWeeksSpent)} weeks spent • ${(life.numberOfWeeksLeft)} weeks left"
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                title = title,
                subtitle = subtitle,
                navigateToProfileScreen = navigateToProfileScreen,
                navigateToAboutScreen = navigateToAboutScreen
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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
    navigateToAboutScreen: () -> Unit
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        },
        actions = {
            NavigationDropDownMenu(
                navigateToProfileScreen = navigateToProfileScreen,
                navigateToAboutScreen = navigateToAboutScreen
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun NavigationDropDownMenu(
    modifier: Modifier = Modifier,
    navigateToProfileScreen: () -> Unit,
    navigateToAboutScreen: () -> Unit
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
                text = { Text("Profile") },
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
                text = { Text("About This App") },
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
        }
    }
}


@Preview
@Composable
private fun HomeTopBarPreview() {
    LifeProgressTheme {
        HomeTopBar(
            title = "Hello World!",
            subtitle = "this is my subtitle",
            navigateToProfileScreen = {},
            navigateToAboutScreen = {}
        )
    }
}

@Preview
@Composable
fun NavigationDropDownMenuPreview() {
    LifeProgressTheme {
        NavigationDropDownMenu(
            navigateToAboutScreen = {},
            navigateToProfileScreen = {}
        )
    }
}