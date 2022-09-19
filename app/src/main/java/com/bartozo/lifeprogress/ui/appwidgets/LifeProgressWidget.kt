package com.bartozo.lifeprogress.ui.appwidgets

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.bartozo.lifeprogress.data.Life

class LifeProgressWidget : GlanceAppWidget() {

    companion object {
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(thinMode, smallMode, mediumMode, largeMode)
    )

    @Composable
    override fun Content() {
        val life = Life.example
        val size = LocalSize.current
        GlanceTheme {
            when (size) {
                thinMode -> LifeCalendarThin(life = life)
                smallMode -> LifeCalendarThin(life = life)
                mediumMode -> LifeCalendarThin(life = life)
                largeMode -> LifeCalendarThin(life = life)
            }
        }
    }
}

@Composable
fun LifeCalendarThin(
    life: Life
) {
    AppWidgetColumn {
        Text(
            text = life.formattedProgress,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Text(
            text = "${life.numberOfWeeksLeft} weeks left",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
        )
        Spacer(modifier = GlanceModifier.size(12.dp))
        Row(modifier = GlanceModifier
            .defaultWeight()
            .fillMaxSize()
            .background(GlanceTheme.colors.primary)
        ) {

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
