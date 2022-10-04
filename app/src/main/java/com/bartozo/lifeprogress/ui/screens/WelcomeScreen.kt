package com.bartozo.lifeprogress.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.WelcomeEventState
import com.bartozo.lifeprogress.ui.viewmodels.WelcomeViewModel
import com.bartozo.lifeprogress.util.supportWideScreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel,
    navigateToProfileScreen: () -> Unit,
) {
    val welcomeEvent: WelcomeEventState by viewModel.welcomeEvent
        .collectAsState()

    LaunchedEffect(key1 = welcomeEvent) {
        when (welcomeEvent) {
            WelcomeEventState.Idle -> {}
            WelcomeEventState.NavigateToProfileScreen -> navigateToProfileScreen()
        }
    }

    var isAppIconVisible by remember { mutableStateOf(false) }
    var isHeadlineVisible by remember { mutableStateOf(false) }
    var isSecondHeadlineVisible by remember { mutableStateOf(false) }
    var isDescriptionVisible by remember { mutableStateOf(false) }
    var isCaptionVisible by remember { mutableStateOf(false) }
    var isButtonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1.seconds)
        isAppIconVisible = true

        delay(1.seconds)
        isHeadlineVisible = true

        delay(1.seconds)
        isSecondHeadlineVisible = true

        delay(2.seconds)
        isDescriptionVisible = true

        delay(2.seconds)
        isCaptionVisible = true

        delay(1.seconds)
        isButtonVisible = true
    }

    Scaffold(
        content = { innerPaddings ->
            WelcomeContent(
                modifier = Modifier
                    .supportWideScreen()
                    .fillMaxHeight()
                    .padding(innerPaddings)
                    .padding(horizontal = 32.dp),
                isAppIconVisible = isAppIconVisible,
                isHeadlineVisible = isHeadlineVisible,
                isSecondHeadlineVisible = isSecondHeadlineVisible,
                isDescriptionVisible = isDescriptionVisible
            )
        },
        bottomBar = {
            WelcomeBottomBar(
                modifier = Modifier
                    .supportWideScreen()
                    .systemBarsPadding()
                    .padding(horizontal = 32.dp, vertical = 20.dp),
                isCaptionVisible = isCaptionVisible,
                isButtonVisible = isButtonVisible,
                onContinueClick = { viewModel.navigateToProfileScreen() }
            )
        },
    )
}

@Composable
private fun WelcomeContent(
    modifier: Modifier = Modifier,
    isAppIconVisible: Boolean,
    isHeadlineVisible: Boolean,
    isSecondHeadlineVisible: Boolean,
    isDescriptionVisible: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        AppearAnimation(isVisible = isAppIconVisible) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                painter = painterResource(id = R.drawable.life_progress_app_icon),
                contentDescription = "App icon"
            )
        }
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            AppearAnimation(isVisible = isHeadlineVisible) {
                Text(
                    "Welcome to",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            AppearAnimation(isVisible = isSecondHeadlineVisible) {
                Text(
                    "Life Progress",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        AppearAnimation(isVisible = isDescriptionVisible) {
            Text(
                "Friendly reminder that you're not gonna live forever.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun WelcomeBottomBar(
    modifier: Modifier = Modifier,
    isCaptionVisible: Boolean,
    isButtonVisible: Boolean,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppearAnimation(isVisible = isCaptionVisible) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Before we continue, let's set up your profile",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        AppearAnimation(isVisible = isButtonVisible) {
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                onClick = onContinueClick
            ) {
                Text(
                    "Continue",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun AppearAnimation(
    isVisible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically() + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
    ) {
        content()
    }
}

@Preview
@Composable
private fun WelcomeContentPreview() {
    LifeProgressTheme {
        WelcomeContent(
            isAppIconVisible = true,
            isHeadlineVisible = true,
            isSecondHeadlineVisible = true,
            isDescriptionVisible = true
        )
    }
}

@Preview
@Composable
private fun WelcomeBottomBarPreview() {
    LifeProgressTheme {
        WelcomeBottomBar(
            isCaptionVisible = true,
            isButtonVisible = true,
            onContinueClick = {}
        )
    }
}
