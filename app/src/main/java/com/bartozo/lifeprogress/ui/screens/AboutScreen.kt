package com.bartozo.lifeprogress.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.components.*
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
                HowItWorksSection(life = life)
                LearnMoreSection(onOpenUrl = { uriHandler.openUri(it) })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HowItWorksSection(
    modifier: Modifier = Modifier,
    life: Life
) {
    SectionHeader(
        modifier = modifier.fillMaxWidth(),
        title = "How it works",
        section = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(),
            ) {
                ListItem(
                    headlineText = {
                        Text(
                            text = "A calendar for your life",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            text = "Each square you see on screen represents a week in your life." +
                                    " The first square (the one at the top left) is the week you were born.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(horizontal = 20.dp, vertical = 32.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(2f)) {
                        SimplifiedLifeCalendar(
                            modifier = Modifier
                                .align(alignment = Alignment.BottomCenter)
                                .offset(x = 50.dp, y = 50.dp),
                            life = life
                        )
                        ZoomedInCalendar(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .height(100.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                ListItem(
                    headlineText = {
                        Text(
                            text = "Each row of 52 weeks makes up one year",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            text = "This is what your current year looks like," +
                                    " see if you can spot it on the calendar.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 20.dp, vertical = 32.dp)
                ) {
                    CurrentYearProgress(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.Center),
                        life = life
                    )
                }
                ListItem(
                    headlineText = {
                        Text(
                            text = "Last thing!",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            text = "Try tapping on the calendar and see what happens.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearnMoreSection(
    modifier: Modifier = Modifier,
    onOpenUrl: (String) -> Unit
) {
    SectionHeader(
        modifier = modifier.fillMaxWidth(),
        title = "Learn more",
        section = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(),
            ) {
                ListItem(
                    modifier = Modifier.clickable {
                        onOpenUrl("https://waitbutwhy.com/2014/05/life-weeks.html")
                    },
                    headlineText = {
                        Text(
                            text = "\"Your Life in Weeks\"",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            buildAnnotatedString {
                                append("This idea was originally introduced in an article by ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Tim Urban")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    append("\nVisit the article")
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                ListItem(
                    modifier = Modifier.clickable {
                        onOpenUrl("https://www.youtube.com/watch?v=JXeJANDKwDc")
                    },
                    headlineText = {
                        Text(
                            text = "\"What Are You Doing With Your Life? The Tail End\"",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            buildAnnotatedString {
                                append("This idea was originally introduced in an article by ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Kurzgesagt")
                                }
                                append("'s phenomenal video on the topic.")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    append("\nSee the video on YouTube")
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                ListItem(
                    modifier = Modifier.clickable {
                        onOpenUrl("https://github.com/Bartozo/Life-Progress-Android")
                    },
                    headlineText = {
                        Text(
                            text = "The project is open source!",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            buildAnnotatedString {
                                append("Learn how this project was created and contribute to it")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    append("\nCheck out the code on GitHub")
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AboutTopBarPreview() {
    AboutTopBar(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onBackButtonClick = {}
    )
}

@Preview
@Composable
private fun HowItWorksSectionPreview() {
    HowItWorksSection(life = Life.example)
}

@Preview
@Composable
private fun LearnMoreSectionPreview() {
    LearnMoreSection(onOpenUrl = {})
}