package com.bartozo.lifeprogress.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.util.supportWideScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navigateToHomeScreen: () -> Unit
) {
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
                    .padding(horizontal = 32.dp, vertical = 20.dp),
                onContinueClick = navigateToHomeScreen
            )
        },
    )
}

@Composable
private fun WelcomeContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            imageVector = Icons.Default.AccountBox,
            contentDescription = "App icon"
        )

        Column(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                "Welcome to",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                "Life Progress",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Text(
            "Friendly reminder that you're not gonna live forever.",
            style = MaterialTheme.typography.bodyLarge,
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Before we continue, let's set up your profile",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
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

@Preview
@Composable
private fun WelcomeScreenPreview() {
    LifeProgressTheme {
        WelcomeScreen(navigateToHomeScreen = {})
    }
}