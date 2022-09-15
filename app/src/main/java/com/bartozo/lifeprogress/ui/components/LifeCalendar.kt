package com.bartozo.lifeprogress.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bartozo.lifeprogress.data.AgeGroup
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import kotlin.math.min
import kotlin.math.roundToInt

enum class LifeCalendarDisplayMode {
    CURRENT_YEAR,
    LIFE;
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CanvasLifeCalendar(
    modifier: Modifier = Modifier,
    life: Life,
    displayMode: LifeCalendarDisplayMode,
) {
    val aspectRatio: Float by animateFloatAsState(
        when (displayMode) {
            LifeCalendarDisplayMode.CURRENT_YEAR -> {
                val currentYearModeColumnCount = 6
                currentYearModeColumnCount.toFloat() /
                        (Life.totalWeeksInAYear / currentYearModeColumnCount.toFloat()).roundToInt()
            }
            LifeCalendarDisplayMode.LIFE -> Life.totalWeeksInAYear / life.lifeExpectancy.toFloat()
        }
    )

    Box(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            AnimatedContent(targetState = displayMode) { displayMode ->
                if (displayMode == LifeCalendarDisplayMode.LIFE) {
                    CalendarWithoutCurrentYear(
                        modifier = Modifier
                            .animateEnterExit(enter = scaleIn(), exit = scaleOut())
                            .aspectRatio(aspectRatio),
                        life = life
                    )
                }
                CalendarWithCurrentYear(
                    modifier = Modifier
                        .animateEnterExit(enter = scaleIn(), exit = scaleOut())
                        .aspectRatio(aspectRatio),
                    life = life,
                    displayMode = displayMode
                )
            }
        }
    }
}

@Composable
private fun CalendarWithoutCurrentYear(
    modifier: Modifier = Modifier,
    life: Life,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val cellSize = canvasWidth / Life.totalWeeksInAYear
        val cellPadding = cellSize / 12

        for (yearIndex in 0..life.lifeExpectancy) {
            for (weekIndex in 0..Life.totalWeeksInAYear) {
                val cellOffset = Offset(
                    x = weekIndex * cellSize + cellPadding,
                    y = yearIndex * cellSize + cellPadding
                )
                val currentYear = yearIndex + 1
                val ageGroupColor = AgeGroup.getGroupForAge(currentYear)
                    .getColor()
                val cellColor = if (currentYear < life.age) {
                    ageGroupColor
                } else if (currentYear > life.age) {
                    Color.Gray
                } else {
                    Color.Transparent
                }

                drawRoundRect(
                    color = cellColor,
                    topLeft = cellOffset,
                    cornerRadius = CornerRadius(
                        x = min(cellSize * 0.16f, 16f),
                        y = min(cellSize * 0.16f, 16f)
                    ),
                    size = Size(
                        width = cellSize - cellPadding * 2,
                        height = cellSize - cellPadding * 2
                    )
                )
            }
        }
    }
}

@Composable
private fun CalendarWithCurrentYear(
    modifier: Modifier = Modifier,
    life: Life,
    displayMode: LifeCalendarDisplayMode,
    currentYearModeColumnCount: Int = 6
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val cellSize = when (displayMode) {
            LifeCalendarDisplayMode.CURRENT_YEAR -> canvasWidth / currentYearModeColumnCount
            LifeCalendarDisplayMode.LIFE -> canvasWidth / Life.totalWeeksInAYear
        }
        val cellPadding = cellSize / 12

        for (weekIndex in 0..Life.totalWeeksInAYear) {
            val rowIndex = when (displayMode) {
                LifeCalendarDisplayMode.CURRENT_YEAR -> weekIndex / currentYearModeColumnCount
                LifeCalendarDisplayMode.LIFE -> life.age - 1
            }
            val columndIndex = when (displayMode) {
                LifeCalendarDisplayMode.CURRENT_YEAR -> weekIndex % currentYearModeColumnCount
                LifeCalendarDisplayMode.LIFE -> weekIndex
            }
            val cellOffset = Offset(
                x = columndIndex * cellSize + cellPadding,
                y = rowIndex * cellSize + cellPadding
            )

            val cellColor = if (weekIndex < life.weekOfYear) {
                AgeGroup.getGroupForAge(life.age + 1).getColor()
            } else {
                Color.Gray
            }

            drawRoundRect(
                color = cellColor,
                topLeft = cellOffset,
                cornerRadius = CornerRadius(
                    x = min(cellSize * 0.16f, 16f),
                    y = min(cellSize * 0.16f, 16f)
                ),
                size = Size(
                    width = cellSize - cellPadding * 2,
                    height = cellSize - cellPadding * 2
                )
            )
        }
    }
}

@Preview
@Composable
private fun CanvasLifeCalendarPreview() {
    LifeProgressTheme {
        CanvasLifeCalendar(
            life = Life.example,
            displayMode = LifeCalendarDisplayMode.LIFE
        )
    }
}
