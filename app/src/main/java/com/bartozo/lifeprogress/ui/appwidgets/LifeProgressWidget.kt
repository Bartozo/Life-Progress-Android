package com.bartozo.lifeprogress.ui.appwidgets

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.Text
import com.bartozo.lifeprogress.data.AgeGroup
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.data.LifeState
import kotlin.math.abs

class LifeProgressWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    // Override the state definition to use our custom one using Kotlin serialization
    override val stateDefinition = LifeProgressStateDefinition

    @Composable
    override fun Content() {
        // Get the stored stated based on our custom state definition.
        val lifeState = currentState<LifeState>()

        val size = LocalSize.current
        GlanceTheme {
            when (lifeState) {
                is LifeState.Available -> {
                    val life = Life(
                        age = lifeState.age,
                        lifeExpectancy = lifeState.lifeExpectancy,
                        weekOfYear = lifeState.weekOfYear
                    )
                    LifeCalendarThin(life = life)
                }
                is LifeState.Loading -> CircularProgressIndicator()
                is LifeState.Unavailable -> {
                    AppWidgetColumn(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Data not available ${lifeState.message}")
//                        Button("Refresh", actionRunCallback<UpdateWeatherAction>())
                    }
                }
            }
        }
    }
}

@Composable
fun LifeCalendarThin(
    life: Life
) {
    AppWidgetColumn {
//        Text(
//            text = life.formattedProgress,
//            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
//        )
//        Text(
//            text = "${life.numberOfWeeksLeft} weeks left",
//            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
//        )
//        Spacer(modifier = GlanceModifier.size(12.dp))
//        Box {
//           Text(text = LocalSize.current.toString())
//        }
        LifeCalendar(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            life = life
        )
    }
}

@Composable
fun LifeCalendar(
    modifier: GlanceModifier = GlanceModifier,
    life: Life
) {
    val size = LocalSize.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        val progressWithoutCurrentYear = life.age / life.lifeExpectancy.toFloat()
        for (group in AgeGroup.values().reversed()) {
            val groupProportion = group.getAgeInRange().last / life.lifeExpectancy.toFloat()
            val groupFullHeight = size.height.value * groupProportion
            val currentAgeInGroup = abs(group.getAgeInRange().first - life.age)
            val totalYearsInGroup = group.getAgeInRange().last - group.getAgeInRange().first

            val color = if (life.age > group.age) {
                group.getColor()
            } else {
                Color.Gray
            }

//            val height: Float = if (life.age > group.getAgeInRange().last) {
//                groupFullHeight
//            } else {
//                val previousGroup = AgeGroup.values().getOrNull(group.ordinal - 1)
//                if (previousGroup != null) {
//                    val previousGroupProportion = previousGroup.getAgeInRange().last /
//                            life.lifeExpectancy.toFloat()
//                    val previousGroupHeight = size.height.value * previousGroupProportion
//
//                    val groupHeight = groupFullHeight - previousGroupHeight
//
//                    groupFullHeight - groupHeight + (groupHeight * (currentAgeInGroup.toFloat() / totalYearsInGroup.toFloat()))
//                } else {
//                    groupFullHeight
//                }
//            }

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
                    val previousGroupHeight = size.height.value * previousGroupProportion

                    val groupHeight = groupFullHeight - previousGroupHeight

                    val height = groupFullHeight - groupHeight + (groupHeight * (currentAgeInGroup.toFloat() / totalYearsInGroup.toFloat()))

                    // Draw full group progress
                    Row(modifier = GlanceModifier.fillMaxWidth()
                        .height(height.dp)
                        .background(color)
                    ) {
                    }

                    // Current year progress
//                    Row(modifier = GlanceModifier.size(
//                        height = groupFullHeight.dp,
//                        width = size.width * (currentAgeInGroup.toFloat() / totalYearsInGroup.toFloat())
//                    ).background(color)
//                    ) {
//
//                    }
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

fun GlanceModifier.appWidgetInnerCornerRadius(): GlanceModifier {
    if (Build.VERSION.SDK_INT >= 31) {
        cornerRadius(android.R.dimen.system_app_widget_inner_radius)
    } else {
        cornerRadius(8.dp)
    }
    return this
}
