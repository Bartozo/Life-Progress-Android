package com.bartozo.lifeprogress.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.ui.components.Header
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
                Header(
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp),
                    text = "User"
                )
                BirthDayCard(
                    modifier = Modifier.padding(top = 16.dp),
                    birthDay = birthDay ?: LocalDate.now(),
                    onBirthDaySelect = { viewModel.updateBirthDay(it) }
                )
                LifeExpectancyCard(
                    modifier = Modifier.padding(top = 30.dp),
                    lifeExpectancy = lifeExpectancy ?: 30,
                    onLifeExpectancySelect = { viewModel.updateLifeExpectancy(it) }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Header(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = "Themes"
                )
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
private fun BirthDayCard(
    modifier: Modifier = Modifier,
    birthDay: LocalDate,
    onBirthDaySelect: (LocalDate) -> Unit
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            negativeButton(
                text = "Cancel",
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        },
        shape = RoundedCornerShape(28.dp),
        onCloseRequest =  { dialogState.hide() },
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        datepicker(
            initialDate = birthDay,
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.surface,
                headerTextColor = MaterialTheme.colorScheme.onSurface,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                dateInactiveTextColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) { date ->
            onBirthDaySelect(date)
        }
    }

    ListItem(
        modifier = modifier.clickable { dialogState.show() },
        leadingContent = {
             Icon(
                 imageVector = Icons.Outlined.Cake,
                 contentDescription = "Cake Icon",
                 tint = MaterialTheme.colorScheme.onSurfaceVariant
             )
        },
        headlineText = {
            Text(
                text = "Your Birthday",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingText = {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = birthDay.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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

    ListItem(
        modifier = modifier,
        headlineText = {
            Text(
                text = "Life Expectancy",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Face,
                contentDescription = "Face Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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