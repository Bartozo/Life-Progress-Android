package com.bartozo.lifeprogress.ui.components.welcome

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.ui.screens.*
import com.bartozo.lifeprogress.util.supportWideScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Welcome(
    modifier: Modifier = Modifier,
    title: String,
    titleTextStyle: TextStyle,
    secondaryTitle: String?,
    secondaryTextStyle: TextStyle,
    features: List<Feature>,
    primaryActionColors: PrimaryActionColors = PrimaryActionDefaults.primaryActionColors(),
    onPrimaryActionClicked: () -> Unit,
    onSecondaryActionClick: (() -> Unit)? = null,
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            WelcomeTopBar(scrollBehavior = scrollBehavior)
        },
        content = { innerPaddings ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPaddings)
                    .supportWideScreen(),
                state = listState
            ) {
                item {
                    Header(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                bottom = 32.dp
                            ),
                        title = title,
                        titleTextStyle = titleTextStyle,
                        secondaryTitle = secondaryTitle,
                        secondaryTextStyle =secondaryTextStyle
                    )
                }
                items(features.size) { index ->
                    val feature = features[index]

                    FeatureListItem(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        feature = feature
                    )
                }
            }
        },
        bottomBar = {
            WelcomeBottomBar(
                listState = listState,
                primaryActionColors = primaryActionColors,
                onPrimaryActionClicked = onPrimaryActionClicked
            )
        }
    )
}

@Composable
private fun LazyListState.isAtBottom(): Boolean {
    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                        lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WelcomeTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = "")
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun WelcomeBottomBar(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    primaryActionColors: PrimaryActionColors,
    onPrimaryActionClicked: () -> Unit,
) {
    val tonalElevation: Float by animateFloatAsState(
        if (listState.isAtBottom()) {
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
            PrimaryAction(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 32.dp
                    ),
                onClick = onPrimaryActionClicked,
                colors = primaryActionColors
            ) {
                Text(stringResource(id = R.string.continue_button_text))
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    title: String,
    titleTextStyle: TextStyle,
    secondaryTitle: String?,
    secondaryTextStyle: TextStyle
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = titleTextStyle
        )
        if (secondaryTitle != null) {
            Text(
                text = secondaryTitle,
                style = secondaryTextStyle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeatureListItem(
    modifier: Modifier = Modifier,
    feature: Feature,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                imageVector = feature.image,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(feature.title)
        },
        supportingContent = {
            Text(feature.subtitle)
        }
    )

}

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
private fun WelcomePreview() {
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
        onPrimaryActionClicked = {},
    )
}