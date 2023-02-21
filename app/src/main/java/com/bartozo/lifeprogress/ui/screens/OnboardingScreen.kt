package com.bartozo.lifeprogress.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.ui.components.BirthDayCard
import com.bartozo.lifeprogress.ui.components.LifeExpectancyCard
import com.bartozo.lifeprogress.ui.components.welcome.*
import com.bartozo.lifeprogress.ui.viewmodels.OnboardingEventState
import com.bartozo.lifeprogress.ui.viewmodels.OnboardingUiState
import com.bartozo.lifeprogress.ui.viewmodels.OnboardingViewModel
import com.bartozo.lifeprogress.util.supportWideScreen
import java.time.LocalDate

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    navigateToHomeScreen: () -> Unit
) {
    val uiState: OnboardingUiState by viewModel.uiState.collectAsState()
    val eventState: OnboardingEventState by viewModel.eventState.collectAsState()
    val birthDayState: LocalDate? by viewModel.birthDay.collectAsState()
    val lifeExpectancyState: Int? by viewModel.lifeExpectancy.collectAsState()

    LaunchedEffect(eventState) {
        when (eventState) {
            is OnboardingEventState.Idle -> {}
            is OnboardingEventState.NavigateToHomeScreen -> navigateToHomeScreen()
        }
    }

    OnboardingUiScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        birthDay = birthDayState,
        lifeExpectancy = lifeExpectancyState,
        onBirthDaySelected = { birthday ->
            viewModel.updateBirthDay(birthday)
        },
        onLifeExpectancySelected = { lifeExpectancy ->
            viewModel.updateLifeExpectancy(lifeExpectancy)
        },
        onContinueClicked = {
            when (uiState) {
                is OnboardingUiState.WelcomeState -> viewModel.moveToBirthday()
                is OnboardingUiState.BirthdayState -> viewModel.moveToLifeExpectancy()
                is OnboardingUiState.LifeExpectancyState -> viewModel.navigateToHome()
            }
        }
    )
}

@Composable
private fun OnboardingUiScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingUiState,
    birthDay: LocalDate?,
    lifeExpectancy: Int?,
    onBirthDaySelected: (LocalDate) -> Unit,
    onLifeExpectancySelected: (Int) -> Unit,
    onContinueClicked: () -> Unit,
) {
    Crossfade(targetState = uiState) { state ->
        when (state) {
            is OnboardingUiState.WelcomeState -> WelcomeSection(
                modifier = modifier,
                onContinueClicked = onContinueClicked
            )
            is OnboardingUiState.BirthdayState -> BirthDaySection(
                modifier = modifier,
                birthDay = birthDay,
                onBirthDaySelected = onBirthDaySelected,
                onContinueClicked = onContinueClicked
            )
            is OnboardingUiState.LifeExpectancyState -> LifeExpectancySection(
                modifier = modifier,
                lifeExpectancy = lifeExpectancy,
                onLifeExpectancySelected = onLifeExpectancySelected,
                onContinueClicked = onContinueClicked
            )
        }
    }
}

@Composable
private fun OnboardingBottomBar(
    modifier: Modifier = Modifier,
    isButtonEnabled: Boolean,
    scrollState: ScrollState,
    onClicked: () -> Unit
) {
    val endReached by remember {
        derivedStateOf {
            scrollState.value == scrollState.maxValue
        }
    }

    val tonalElevation: Float by animateFloatAsState(
        if (endReached) {
            0f
        } else {
            4f
        }
    )

    Surface(
        modifier = modifier.navigationBarsPadding(),
        tonalElevation = tonalElevation.dp
    ) {
        Row(modifier = Modifier.supportWideScreen()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 32.dp
                    ),
                enabled = isButtonEnabled,
                onClick = onClicked,
            ) {
                Text(stringResource(id = R.string.continue_button_text))
            }
        }
    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
private fun WelcomeSection(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
) {
    Welcome(
        modifier = modifier,
        title = stringResource(id = R.string.welcome_to),
        titleTextStyle = MaterialTheme.typography.displaySmall,
        secondaryTitle = stringResource(id = R.string.app_name),
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
                title = stringResource(id = R.string.measure_life_feature_title),
                subtitle = stringResource(id = R.string.measure_life_feature_description),
                image = Icons.Outlined.DateRange
            ),
            Feature(
                title = stringResource(id = R.string.widget_feature_title),
                subtitle = stringResource(id = R.string.widget_feature_description),
                image = Icons.Outlined.Widgets
            ),
            Feature(
                title = stringResource(id = R.string.theme_feature_title),
                subtitle = stringResource(id = R.string.theme_feature_description),
                image = Icons.Outlined.Palette
            ),
            Feature(
                title = stringResource(id = R.string.orientations_feature_title),
                subtitle = stringResource(id = R.string.orientations_feature_description),
                image = Icons.Outlined.Devices
            )
        ),
        primaryActionColors = PrimaryActionColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onPrimaryActionClicked = onContinueClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDaySection(
    modifier: Modifier = Modifier,
    birthDay: LocalDate?,
    onBirthDaySelected: (LocalDate) -> Unit,
    onContinueClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "")
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPaddings ->
            Column(
                modifier = modifier
                    .padding(innerPaddings)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.select_your_birthday_title),
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.supportWideScreen(),
                    text = stringResource(id = R.string.select_your_birthday_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                BirthDayCard(
                    modifier = Modifier.supportWideScreen(),
                    birthDay = birthDay ?: LocalDate.now(),
                    onBirthDaySelect = onBirthDaySelected
                )
            }
        },
        bottomBar = {
            OnboardingBottomBar(
                isButtonEnabled = birthDay != null,
                scrollState = scrollState,
                onClicked = onContinueClicked
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LifeExpectancySection(
    modifier: Modifier = Modifier,
    lifeExpectancy: Int?,
    onLifeExpectancySelected: (Int) -> Unit,
    onContinueClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "")
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPaddings ->
            Column(
                modifier = modifier
                    .padding(innerPaddings)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.select_your_life_expectancy_title),
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.supportWideScreen(),
                    text = stringResource(id = R.string.select_your_life_expectancy_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                LifeExpectancyCard(
                    modifier = Modifier.supportWideScreen(),
                    lifeExpectancy = lifeExpectancy ?: 90,
                    onLifeExpectancySelect = onLifeExpectancySelected
                )
            }
        },
        bottomBar = {
            OnboardingBottomBar(
                isButtonEnabled = lifeExpectancy != null,
                scrollState = scrollState,
                onClicked = onContinueClicked
            )
        }
    )
}