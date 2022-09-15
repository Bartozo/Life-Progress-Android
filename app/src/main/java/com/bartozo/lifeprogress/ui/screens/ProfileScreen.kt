package com.bartozo.lifeprogress.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.ui.components.WorkInProgressCard
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.ProfileViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateBackToHomeScreen: () -> Unit
) {
    val birthDay: LocalDate? by viewModel.birthDay
        .collectAsState(initial = LocalDate.now())
    val lifeExpectancy: Int? by viewModel.lifeExpectancy
        .collectAsState(initial = 30)
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ProfileTopBar(
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
                Header(text = "User")
                BirthDayCard(
                    birthDay = birthDay ?: LocalDate.now(),
                    onBirthDaySelect = { viewModel.updateBirthDay(it) }
                )
                LifeExpectancyCard(
                    lifeExpectancy = lifeExpectancy ?: 30,
                    onLifeExpectancySelect = { viewModel.updateLifeExpectancy(it) }
                )
                Divider(modifier = Modifier.padding(horizontal = 8.dp))
                Header(text = "Themes")
                WorkInProgressCard()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackButtonClick: () -> Unit
) {
    LargeTopAppBar(
        modifier = modifier,
        title = { Text("Profile") },
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.return_to_previous_screen)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    modifier: Modifier = Modifier,
    text: String
) {
    ListItem(
        modifier = modifier,
        headlineText = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDayCard(
    modifier: Modifier = Modifier,
    birthDay: LocalDate,
    onBirthDaySelect: (LocalDate) -> Unit
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        },
        onCloseRequest =  { dialogState.hide() },
    ) {
        datepicker(
            initialDate = birthDay,
            colors = DatePickerDefaults.colors(headerBackgroundColor = Color.Red)
        ) { date ->
            onBirthDaySelect(date)
        }
    }

    ListItem(
        modifier = modifier.clickable { dialogState.show() },
        headlineText = {
            Text(
                text = "Your Birthday",
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingText = {
            Text(
                text = birthDay.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
         },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun LifeExpectancyCard(
    modifier: Modifier = Modifier,
    lifeExpectancy: Int,
    onLifeExpectancySelect: (Int) -> Unit
) {
    var sliderPosition by remember { mutableStateOf(lifeExpectancy.toFloat()) }

    LaunchedEffect(key1 = lifeExpectancy) {
        sliderPosition = lifeExpectancy.toFloat()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ListItem(
            headlineText = {
                Text(
                    text = "Life Expectancy",
                    style = MaterialTheme.typography.titleMedium
                )
           },
            leadingContent = {
                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    imageVector = Icons.Outlined.Face,
                    contentDescription = ""
                )
            },
            trailingContent = {
                AnimatedContent(
                    targetState = lifeExpectancy,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()
                        } else {
                            slideInVertically { height -> -height } + fadeIn() with
                                    slideOutVertically { height -> height } + fadeOut()
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }
                ) { targetCount ->
                    Text(
                        modifier = Modifier.width(25.dp),
                        text = "$targetCount",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            },
            supportingText = {
                Slider(
                    modifier = Modifier.offset(
                        x = (-8).dp
                    ),
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        onLifeExpectancySelect(it.roundToInt())
                    },
                    valueRange = 18f..150f,
                    onValueChangeFinished = {
                        onLifeExpectancySelect(sliderPosition.roundToInt())
                    },
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProfileTopBarPreview() {
    LifeProgressTheme {
        ProfileTopBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onBackButtonClick = {}
        )
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    LifeProgressTheme {
        Header(text = "Profile")
    }
}

@Preview
@Composable
private fun BirthDayCardPreview() {
    LifeProgressTheme {
        BirthDayCard(
            birthDay = LocalDate.now(),
            onBirthDaySelect = {}
        )
    }
}

@Preview
@Composable
private fun LifeExpectancyCardPreview() {
   LifeProgressTheme {
       LifeExpectancyCard(
           lifeExpectancy = 100,
           onLifeExpectancySelect = {}
       )
   }
}