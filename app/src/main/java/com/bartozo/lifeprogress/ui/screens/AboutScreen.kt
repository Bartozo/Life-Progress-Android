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
        title = { 
            Text(text = stringResource(id = R.string.about_life_progress))
        },
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
        Header(text = stringResource(id = R.string.how_it_works))
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = stringResource(id = R.string.calendar_for_your_life_title),
            supportingText = stringResource(id = R.string.calendar_for_your_life_description),
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
            headline = stringResource(id = R.string.each_row_title),
            supportingText = stringResource(id = R.string.each_row_description),
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
            headline = stringResource(id = R.string.last_thing_title),
            supportingText = stringResource(id = R.string.last_thing_description)
        )
    }
}

@Composable
private fun LearnMoreSection(
    modifier: Modifier = Modifier,
    onOpenUrl: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(text = stringResource(id = R.string.learn_more))
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = stringResource(id = R.string.your_life_in_weeks_title),
            supportingText = stringResource(id = R.string.your_life_in_weeks_description),
            onClick = {
                onOpenUrl("https://waitbutwhy.com/2014/05/life-weeks.html")
            }
        )
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = stringResource(id = R.string.what_are_you_doing_with_your_life_title),
            supportingText = stringResource(id = R.string.what_are_you_doing_with_your_life_description),
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
        Header(text = stringResource(id = R.string.support))
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            header = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircleWaveAnimation()
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
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
            headline = stringResource(id = R.string.support_project_title),
            supportingText = stringResource(id = R.string.support_project_description),
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