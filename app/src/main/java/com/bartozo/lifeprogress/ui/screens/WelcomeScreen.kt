package com.bartozo.lifeprogress.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.WelcomeEventState
import com.bartozo.lifeprogress.ui.viewmodels.WelcomeViewModel
import com.bartozo.lifeprogress.util.supportWideScreen

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

    Scaffold(
        content = { innerPaddings ->
            WelcomeContent(
                modifier = Modifier
                    .supportWideScreen()
                    .fillMaxHeight()
                    .padding(innerPaddings)
                    .padding(horizontal = 32.dp)
            )
        },
        bottomBar = {
            WelcomeBottomBar(
                modifier = Modifier
                    .supportWideScreen()
                    .systemBarsPadding()
                    .padding(horizontal = 32.dp, vertical = 20.dp),
                onContinueClick = { viewModel.navigateToProfileScreen() }
            )
        },
    )
}

@Composable
private fun WelcomeContent(
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp),
            imageVector = Icons.Default.AccountBox,
            contentDescription = "App icon"
        )

        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                "Welcome to",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Life Progress",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            "Friendly reminder that you're not gonna live forever.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun WelcomeBottomBar(
    modifier: Modifier = Modifier,
    onContinueClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Before we continue, let's set up your profile",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

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

@Preview
@Composable
private fun WelcomeContentPreview() {
    LifeProgressTheme {
        WelcomeContent()
    }
}

@Preview
@Composable
private fun WelcomeBottomBarPreview() {
    LifeProgressTheme {
        WelcomeBottomBar(onContinueClick = {})
    }
}
