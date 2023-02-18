package com.bartozo.lifeprogress.ui.screens

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Notifications
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
import com.bartozo.lifeprogress.util.hasNotificationPermission
import com.bartozo.lifeprogress.util.supportWideScreen
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateBackToHomeScreen: () -> Unit
) {
    val context = LocalContext.current
    val widgetManager = AppWidgetManager.getInstance(context)
    // Get a list of our app widget providers to retrieve their info
    val widgetProviders = widgetManager.getInstalledProvidersForPackage(context.packageName, null)

    val birthDay: LocalDate by viewModel.birthDay
        .collectAsState(initial = LocalDate.now())
    val lifeExpectancy: Int by viewModel.lifeExpectancy
        .collectAsState(initial = 30)
    val life: Life by viewModel.lifeFlow
        .collectAsState(initial = Life.example)
    val appTheme: AppTheme by viewModel.appTheme
        .collectAsState()
    val isWeeklyNotificationEnabled: Boolean by viewModel.isWeeklyNotificationEnabled
        .collectAsState()

    var isNotificationPermissionGranted by remember {
        mutableStateOf(context.hasNotificationPermission())
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isNotificationPermissionGranted = isGranted
    }

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


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
                UserSection(
                    modifier = Modifier.supportWideScreen(),
                    birthDay = birthDay,
                    lifeExpectancy = lifeExpectancy,
                    isWeeklyNotificationEnabled = isWeeklyNotificationEnabled,
                    areNotificationsEnabled = context.hasNotificationPermission(),
                    onBirthDaySelected = { viewModel.updateBirthDay(it, context) },
                    onLifeExpectancySelected = { viewModel.updateLifeExpectancy(it, context) },
                    onIsWeeklyNotificationEnabledChanged = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }

                        viewModel.updateIsWeeklyNotificationEnabled(it, context)
                    }
                )
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .supportWideScreen(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                ThemesSection(
                    modifier = Modifier.supportWideScreen(),
                    appTheme = appTheme,
                    onAppThemeSelected = { viewModel.updateAppTheme(it) }
                )
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .supportWideScreen(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                AppWidgetSection(
                    modifier = Modifier.supportWideScreen(),
                    life = life,
                    isRequestPinAppWidgetSupported = widgetManager.isRequestPinAppWidgetSupported,
                    onPinAppWidgetClicked = {
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
private fun UserSection(
    modifier: Modifier = Modifier,
    birthDay: LocalDate,
    lifeExpectancy: Int,
    isWeeklyNotificationEnabled: Boolean,
    areNotificationsEnabled: Boolean,
    onBirthDaySelected: (LocalDate) -> Unit,
    onLifeExpectancySelected: (Int) -> Unit,
    onIsWeeklyNotificationEnabledChanged: (Boolean) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            text = stringResource(id = R.string.user)
        )
        BirthDayCard(
            modifier = Modifier.padding(top = 16.dp),
            birthDay = birthDay,
            onBirthDaySelect = onBirthDaySelected
        )
        LifeExpectancyCard(
            modifier = Modifier.padding(top = 16.dp),
            lifeExpectancy = lifeExpectancy,
            onLifeExpectancySelect = onLifeExpectancySelected
        )
        if (!areNotificationsEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                    text = "Notifications are disabled, because permission was not granted.",
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
        ListItem(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .clickable(enabled = areNotificationsEnabled) {
                    onIsWeeklyNotificationEnabledChanged(!isWeeklyNotificationEnabled)
                },
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            headlineText = {
                Text(
                    text = "Weekly notification",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingText = {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Receive a weekly notification with your current life progress",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingContent = {
                Switch(
                    enabled = areNotificationsEnabled,
                    checked = isWeeklyNotificationEnabled,
                    onCheckedChange = onIsWeeklyNotificationEnabledChanged
                )
            }
        )
    }
}

@Composable
private fun ThemesSection(
    modifier: Modifier = Modifier,
    appTheme: AppTheme,
    onAppThemeSelected: (AppTheme) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            text = stringResource(id = R.string.themes)
        )
        Themes(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            selectedAppTheme = appTheme,
            onAppThemeSelected = onAppThemeSelected
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AppWidgetSection(
    modifier: Modifier = Modifier,
    life: Life,
    isRequestPinAppWidgetSupported: Boolean,
    onPinAppWidgetClicked: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Header(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            text = stringResource(id = R.string.app_widgets)
        )
        AppWidgetCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            life = life,
            isRequestPinAppWidgetSupported = isRequestPinAppWidgetSupported,
            onPinAppWidgetClick = onPinAppWidgetClicked
        )
    }
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
