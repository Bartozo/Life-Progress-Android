package com.bartozo.lifeprogress.ui.screens

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.data.AppTheme
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.data.LifeState
import com.bartozo.lifeprogress.data.appThemes
import com.bartozo.lifeprogress.ui.appwidgets.AppWidgetPinnedReceiver
import com.bartozo.lifeprogress.ui.appwidgets.LifeProgressWidget
import com.bartozo.lifeprogress.ui.appwidgets.LifeProgressWidgetReceiver
import com.bartozo.lifeprogress.ui.components.*
import com.bartozo.lifeprogress.ui.theme.DarkTheme
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import com.bartozo.lifeprogress.ui.theme.LightTheme
import com.bartozo.lifeprogress.ui.viewmodels.ProfileViewModel
import com.bartozo.lifeprogress.util.rangeOfYearsFromNowTo
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateBackToHomeScreen: () -> Unit
) {
    val birthDay: LocalDate by viewModel.birthDay
        .collectAsState(initial = LocalDate.now())
    val lifeExpectancy: Int by viewModel.lifeExpectancy
        .collectAsState(initial = 30)
    val life: Life by viewModel.lifeFlow
        .collectAsState(initial = Life.example)
    val appTheme: AppTheme by viewModel.appTheme
        .collectAsState()

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val context = LocalContext.current
    val widgetManager = AppWidgetManager.getInstance(context)
    // Get a list of our app widget providers to retrieve their info
    val widgetProviders = widgetManager.getInstalledProvidersForPackage(context.packageName, null)

    BackHandler(onBack = navigateBackToHomeScreen)

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
                    text = stringResource(id = R.string.user)
                )
                BirthDayCard(
                    modifier = Modifier.padding(top = 16.dp),
                    birthDay = birthDay,
                    onBirthDaySelect = { viewModel.updateBirthDay(it, context) }
                )
                LifeExpectancyCard(
                    modifier = Modifier.padding(top = 30.dp),
                    lifeExpectancy = lifeExpectancy,
                    onLifeExpectancySelect = { viewModel.updateLifeExpectancy(it, context) }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Header(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = stringResource(id = R.string.themes)
                )
                Themes(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    selectedAppTheme = appTheme,
                    onAppThemeSelected = { viewModel.updateAppTheme(it) }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Header(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = stringResource(id = R.string.app_widgets)
                )
                AppWidgetCard(
                    modifier =  Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    life = life,
                    isRequestPinAppWidgetSupported = widgetManager.isRequestPinAppWidgetSupported,
                    onPinAppWidgetClick = {
                        // The application has only one widget
                        widgetProviders.first().pin(context, life)
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
        title = { 
            Text(text = stringResource(id = R.string.profile))
        },
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
                text = stringResource(id = com.google.android.material.R.string.mtrl_picker_confirm),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            negativeButton(
                text = stringResource(id = com.google.android.material.R.string.mtrl_picker_cancel),
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
                text = stringResource(id = R.string.your_birthday),
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
                text = stringResource(id = R.string.life_expectancy),
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
private fun Themes(
    modifier: Modifier = Modifier,
    selectedAppTheme: AppTheme,
    onAppThemeSelected: (AppTheme) -> Unit
) {
    val themes = appThemes

    Row(modifier = modifier.fillMaxWidth()) {
        for (theme in themes) {
            ThemeButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                isSelected = theme == selectedAppTheme,
                appTheme = theme,
                onAppThemeSelected = onAppThemeSelected
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ThemeButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    appTheme: AppTheme,
    onAppThemeSelected: (AppTheme) -> Unit
) {
    val borderColor: Color by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        }
    )

    val backgroundColor: Color by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
    )

    val textColor: Color by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.27f)
                .clip(RoundedCornerShape(size = 16.dp))
                .clickable { onAppThemeSelected(appTheme) },
            shape = RoundedCornerShape(size = 16.dp),
            border = BorderStroke(width = 2.6.dp, color = borderColor),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (appTheme) {
                    AppTheme.SYSTEM_AUTO -> {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            LightTheme {
                                Row(modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.surface)) {
                                }
                            }
                            DarkTheme {
                                Row(modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.surface)) {
                                }
                            }
                            DarkTheme {
                                Row(modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.surface)) {
                                }
                            }
                        }
                    }
                    AppTheme.LIGHT -> {
                        LightTheme {
                            Row(modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)) {
                            }
                        }
                    }
                    AppTheme.DARK -> {
                        DarkTheme {
                            Row(modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)) {
                            }
                        }
                    }
                }
                Column(modifier = Modifier.align(Alignment.Center)) {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        FancyIcon(
                            icon = Icons.Filled.Check,
                            contentDescription = "Check icon"
                        )
                    }
                }
            }
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(id = appTheme.getLocalizedString()),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}


@ExperimentalComposeUiApi
@Composable
private fun AppWidgetCard(
    modifier: Modifier = Modifier,
    life: Life,
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
            headline = stringResource(id = R.string.life_calendar_card_title),
            supportingText = stringResource(id = R.string.life_calendar_card_description),
            onClick = onPinAppWidgetClick,
            header = {
                Box(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 48.dp,
                            end = 48.dp
                        )
                        .offset(y = 16.dp) // Hide the bottom line of the border
                        .fillMaxSize()
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Row {
                                val iconSize = MaterialTheme.typography.labelSmall.fontSize.value.dp
                                Icon(
                                    modifier = Modifier.size(iconSize),
                                    imageVector = Icons.Filled.NetworkWifi3Bar,
                                    contentDescription = "Wifi Icon",
                                )
                                Icon(
                                    modifier = Modifier.size(iconSize),
                                    imageVector = Icons.Filled.SignalCellular4Bar,
                                    contentDescription = "Signal Cellular Icon",
                                )
                                Icon(
                                    modifier = Modifier.size(iconSize),
                                    imageVector = Icons.Filled.Battery3Bar,
                                    contentDescription = "Battery Icon",
                                )
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(top = 8.dp, bottom = 16.dp)
                            ) {
                                Card {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = life.formattedProgress,
                                            style = MaterialTheme.typography.labelMedium,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = pluralStringResource(
                                                id = R.plurals.weeks_spent,
                                                count = life.numberOfWeeksSpent,
                                                life.numberOfWeeksSpent
                                            ),
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        SimplifiedLifeCalendar(life = life)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
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
private fun AppWidgetProviderInfo.pin(context: Context, life: Life) {
    val successCallback = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, AppWidgetPinnedReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val manager = GlanceAppWidgetManager(context)
    manager.requestPinGlanceAppWidget(
        receiver = LifeProgressWidgetReceiver::class.java,
        preview = LifeProgressWidget(),
        previewState = LifeState.Available(
            age = life.age,
            lifeExpectancy = life.lifeExpectancy,
            weekOfYear = life.weekOfYear
        ),
        successCallback = successCallback
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

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun AppWidgetCardPreview() {
    LifeProgressTheme {
        AppWidgetCard(
            life = Life.example,
            isRequestPinAppWidgetSupported = false,
            onPinAppWidgetClick = {}
        )
    }
}

@Preview
@Composable
fun ThemesPreview() {
    LifeProgressTheme {
        Themes(
            selectedAppTheme = AppTheme.SYSTEM_AUTO,
            onAppThemeSelected = {}
        )
    }
}
