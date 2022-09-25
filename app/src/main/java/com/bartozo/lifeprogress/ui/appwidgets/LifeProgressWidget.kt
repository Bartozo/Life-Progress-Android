package com.bartozo.lifeprogress.ui.appwidgets

import android.content.Context
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.bartozo.lifeprogress.data.AgeGroup
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.data.LifeState
import kotlin.math.abs

private const val WIDGET_PADDING = 16

class LifeProgressWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    // Override the state definition to use our custom one using Kotlin serialization
    override val stateDefinition = LifeProgressStateDefinition

    @Composable
    override fun Content() {
        // Get the stored stated based on our custom state definition.
        val lifeState = currentState<LifeState>()

        GlanceTheme {
            when (lifeState) {
                is LifeState.Available -> {
                    AppWidgetColumn {
                        LifeCalendarThin(
                            life = Life(
                                age = lifeState.age,
                                lifeExpectancy = lifeState.lifeExpectancy,
                                weekOfYear = lifeState.weekOfYear
                            )
                        )
                    }
                }
                is LifeState.Loading -> Box {
                    AppWidgetBox(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is LifeState.Unavailable -> {
                    AppWidgetColumn(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Data not available ${lifeState.message}")
                        Button("Refresh", actionRunCallback<UpdateLifeProgressAction>())
                    }
                }
            }
        }
    }
}

@Composable
fun LifeCalendarThin(
    modifier: GlanceModifier = GlanceModifier,
    life: Life
) {
    Column(modifier = modifier) {
        Header(
            modifier = GlanceModifier.fillMaxWidth(),
            life = life
        )
        LifeCalendar(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            life = life
        )
    }
}

@Composable
fun Header(
    modifier: GlanceModifier = GlanceModifier,
    life: Life
) {
    Column(modifier = modifier) {
        val header = MaterialTheme.typography.titleMedium
        val body = MaterialTheme.typography.bodyMedium

        Text(
            text = life.formattedProgress,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = header.fontSize,
                color = GlanceTheme.colors.textColorPrimary,
            ),
            maxLines = 1
        )
        Text(
            text = "${life.numberOfWeeksLeft} weeks left",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = body.fontSize,
                color = GlanceTheme.colors.textColorSecondary,
            ),
            maxLines = 1
        )
        Spacer(modifier = GlanceModifier.size(12.dp))
    }
}

@Composable
fun LifeCalendar(
    modifier: GlanceModifier = GlanceModifier,
    life: Life
) {
    // TODO - refactor this code to use weight in the future glance versions
    val size = LocalSize.current
    val headerFontSize = MaterialTheme.typography.titleMedium.fontSize.value
    val headerSubtitleFontSize = MaterialTheme.typography.bodyMedium.fontSize.value
    val widgetHeight = size.height.value - headerFontSize - headerSubtitleFontSize - (2 * WIDGET_PADDING)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        for (group in AgeGroup.values().reversed()) {
            val groupProportion = group.getAgeInRange().last / life.lifeExpectancy.toFloat()
            val groupFullHeight = widgetHeight * groupProportion
            val currentAgeInGroup = abs(group.getAgeInRange().first - life.age)
            val totalYearsInGroup = group.getAgeInRange().last - group.getAgeInRange().first

            val color = if (life.age > group.age) {
                group.getColor()
            } else {
                Color.Gray
            }

            if (life.age > group.getAgeInRange().last) {
                // Draw full group progress
                Row(modifier = GlanceModifier.fillMaxWidth()
                    .height(groupFullHeight.dp)
                    .background(color)
                ) {
                }
            } else {
                val previousGroup = AgeGroup.values().getOrNull(group.ordinal - 1)
                if (previousGroup != null) {
                    val previousGroupProportion = previousGroup.getAgeInRange().last /
                            life.lifeExpectancy.toFloat()
                    val previousGroupHeight = widgetHeight * previousGroupProportion

                    val groupHeight = groupFullHeight - previousGroupHeight

                    val height = groupFullHeight - groupHeight + (groupHeight * (currentAgeInGroup.toFloat() / totalYearsInGroup.toFloat()))

                    // Draw full group progress
                    Row(modifier = GlanceModifier.fillMaxWidth()
                        .height(height.dp)
                        .background(color)
                    ) {
                    }

                    if (life.currentYearRemainingWeeks < 52) {
                        // Current year progress
                        val currentYearHeight = groupFullHeight - groupHeight + (groupHeight * ((currentAgeInGroup.toFloat() + 1) / totalYearsInGroup.toFloat()))
                        val currentYearHeightWidth = if (life.currentYearRemainingWeeks == Life.totalWeeksInAYear) {
                            size.width
                        } else {
                            size.width * ((Life.totalWeeksInAYear - life.currentYearRemainingWeeks).toFloat() / Life.totalWeeksInAYear)
                        }
                        Row(modifier = GlanceModifier.size(
                            height = currentYearHeight.dp,
                            width =  currentYearHeightWidth
                        ).background(color)
                        ) {

                        }
                    }
                } else {
                    // Draw full group progress
                    Row(modifier = GlanceModifier.fillMaxWidth()
                        .height(groupFullHeight.dp)
                        .background(color)
                    ) {
                    }
                }
            }
        }
    }
}


/**
 * Provide a Column composable using the system parameters for app widgets background with rounded
 * corners and background color.
 */
@Composable
fun AppWidgetColumn(
    modifier: GlanceModifier = GlanceModifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = appWidgetBackgroundModifier().then(modifier),
        verticalAlignment = verticalAlignment,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

/**
 * Provide a Box composable using the system parameters for app widgets background with rounded
 * corners and background color.
 */
@Composable
fun AppWidgetBox(
    modifier: GlanceModifier = GlanceModifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    Box(
        modifier = appWidgetBackgroundModifier().then(modifier),
        contentAlignment = contentAlignment,
        content = content
    )
}


@Composable
fun appWidgetBackgroundModifier() = GlanceModifier
    .fillMaxSize()
    .padding(16.dp)
    .appWidgetBackground()
    .background(GlanceTheme.colors.background)
    .appWidgetBackgroundCornerRadius()


fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier {
    if (Build.VERSION.SDK_INT >= 31) {
        cornerRadius(android.R.dimen.system_app_widget_background_radius)
    } else {
        cornerRadius(16.dp)
    }
    return this
}

/**
 * Force update the weather info after user click
 */
class UpdateLifeProgressAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Force the worker to refresh
        LifeProgressWorker.enqueue(context = context, force = true)
    }
}
