package com.bartozo.lifeprogress.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.components.*
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.AboutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    navigateBackToHomeScreen: () -> Unit
) {
    val scrollState = rememberScrollState()
    val life: Life by viewModel.lifeFlow
        .collectAsState(initial = Life.example)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val uriHandler = LocalUriHandler.current

    BackHandler(onBack = navigateBackToHomeScreen)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AboutTopBar(
                scrollBehavior = scrollBehavior,
                onBackButtonClick = navigateBackToHomeScreen
            )
         },
        content = { innerPaddings ->
            Column(
                modifier = Modifier
                    .padding(innerPaddings)
                    .verticalScroll(state = scrollState)
            ) {
                HowItWorksSection(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                    ),
                    life = life
                )
                LearnMoreSection(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 32.dp
                    ),
                    onOpenUrl = { uriHandler.openUri(it) }
                )
                SupportSection(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 32.dp,
                        bottom = 16.dp
                    ),
                    onOpenUrl = { uriHandler.openUri(it) }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackButtonClick: () -> Unit
) {
    LargeTopAppBar(
        modifier = modifier,
        title = { Text("About Life Progress") },
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigation)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun HowItWorksSection(
    modifier: Modifier = Modifier,
    life: Life
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(text = "How it works")
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = "A calendar for your life",
            supportingText = "Each square you see on screen represents a week in your life." +
                    " The first square (the one at the top left) is the week you were born.",
            header = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f)) {
                        SimplifiedLifeCalendar(
                            modifier = Modifier
                                .align(alignment = Alignment.BottomCenter)
                                .offset(x = 50.dp, y = 50.dp),
                            life = life
                        )
                        ZoomedInCalendar(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .height(75.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        )
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = "Each row of 52 weeks makes up one year",
            supportingText = "This is what your current year looks like," +
                    " see if you can spot it on the calendar.",
            header = {
                Column {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 20.dp, vertical = 32.dp)
                    ) {
                        CurrentYearProgress(
                            modifier = Modifier.fillMaxWidth(),
                            life = life
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        )
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = "Last thing!",
            supportingText = "Try tapping on the calendar and see what happens.",
        )
    }
}

@Composable
private fun LearnMoreSection(
    modifier: Modifier = Modifier,
    onOpenUrl: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(text = "Learn more")
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = "Your Life in Weeks",
            supportingText = "This idea was originally introduced in an article by Tim Urban.",
            onClick = {
                onOpenUrl("https://waitbutwhy.com/2014/05/life-weeks.html")
            }
        )
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = "What Are You Doing With Your Life? The Tail End",
            supportingText = "Kurzgesagt's phenomenal video on the topic.",
            onClick = {
                onOpenUrl("https://www.youtube.com/watch?v=JXeJANDKwDc")
            }
        )
//        InformationCard(
//            modifier = Modifier.padding(top = 16.dp),
//            headline = "The project is open source!",
//            supportingText = "Learn how this project was created and contribute to it." +
//                    " Check out the code on GitHub.",
//            onClick = {
//                onOpenUrl("https://github.com/Bartozo/Life-Progress-Android")
//            }
//        )
    }
}

@Composable
private fun SupportSection(
    modifier: Modifier = Modifier,
    onOpenUrl: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(text = "Support")
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            header = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircleWaveAnimation()
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = Icons.Filled.Coffee,
                            contentDescription = "Coffee Icon",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            headline = "Support this project",
            supportingText = "If you enjoy using this application and want it to has more features, " +
                    "you can support the development of this project by buying me a coffee.",
            onClick = {
                onOpenUrl("https://www.buymeacoffee.com/bartozo")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AboutTopBarPreview() {
    LifeProgressTheme {
        AboutTopBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onBackButtonClick = {}
        )
    }
}

@Preview
@Composable
private fun HowItWorksSectionPreview() {
    LifeProgressTheme {
        HowItWorksSection(life = Life.example)
    }
}

@Preview
@Composable
private fun LearnMoreSectionPreview() {
    LifeProgressTheme{
        LearnMoreSection(onOpenUrl = {})
    }
}

@Preview
@Composable
private fun SupportSectionPreview() {
    LifeProgressTheme{
        SupportSection(onOpenUrl = {})
    }
}