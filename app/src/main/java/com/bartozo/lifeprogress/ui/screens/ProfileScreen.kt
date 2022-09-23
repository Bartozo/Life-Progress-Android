package com.bartozo.lifeprogress.ui.screens

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.appwidgets.AppWidgetPinnedReceiver
import com.bartozo.lifeprogress.ui.components.*
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.viewmodels.ProfileViewModel
import com.bartozo.lifeprogress.util.rangeOfYearsFromNowTo
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

    val context = LocalContext.current
    val widgetManager = AppWidgetManager.getInstance(context)
    // Get a list of our app widget providers to retrieve their info
    val widgetProviders = widgetManager.getInstalledProvidersForPackage(context.packageName, null)

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
                    onBirthDaySelect = { viewModel.updateBirthDay(it, context) }
                )
                LifeExpectancyCard(
                    modifier = Modifier.padding(top = 30.dp),
                    lifeExpectancy = lifeExpectancy ?: 30,
                    onLifeExpectancySelect = { viewModel.updateLifeExpectancy(it, context) }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Header(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = "Themes"
                )
                WorkInProgressCard()
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Header(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = "App widgets"
                )
                AppWidgetCard(
                    modifier =  Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    isRequestPinAppWidgetSupported = widgetManager.isRequestPinAppWidgetSupported,
                    onPinAppWidgetClick = {
                        // The application has only one widget
                        widgetProviders.first().pin(context)
                    }
                )
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
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            yearRange = rangeOfYearsFromNowTo(150),
            allowedDateValidator = {
                it.isBefore(LocalDate.now()) || it.isEqual(LocalDate.now())
            },
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

@Composable
private fun AppWidgetCard(
    modifier: Modifier = Modifier,
    isRequestPinAppWidgetSupported: Boolean,
    onPinAppWidgetClick: () -> Unit
) {

    Column(modifier = modifier) {
        if (!isRequestPinAppWidgetSupported) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.error)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = "Error Icon",
                    tint = MaterialTheme.colorScheme.onError
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.pin_unavailable),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
        InformationCard(
            modifier = Modifier.padding(top = 16.dp),
            headline = "Simplified life calendar",
            supportingText = "See the current life progress with this helpful widget " +
                    "on your home screen.",
            onClick = onPinAppWidgetClick,
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
                            life = Life.example
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        )
    }
}

/**
 * Extension method to request the launcher to pin the given AppWidgetProviderInfo
 *
 * Note: the optional success callback to retrieve if the widget was placed might be unreliable
 * depending on the default launcher implementation. Also, it does not callback if user cancels the
 * request.
 */
private fun AppWidgetProviderInfo.pin(context: Context) {
    val successCallback = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, AppWidgetPinnedReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    AppWidgetManager.getInstance(context).requestPinAppWidget(provider, null, successCallback)
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

@Preview
@Composable
fun AppWidgetCardPreview() {
    LifeProgressTheme {
        AppWidgetCard(
            isRequestPinAppWidgetSupported = false,
            onPinAppWidgetClick = {}
        )
    }
}