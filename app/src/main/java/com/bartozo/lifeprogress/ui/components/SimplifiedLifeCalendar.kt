package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.bartozo.lifeprogress.data.AgeGroup
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun SimplifiedLifeCalendar(
    modifier: Modifier = Modifier,
    life: Life,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
            .clip(RectangleShape)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val cellHeight = canvasHeight / life.lifeExpectancy

        for (yearIndex in 0 until life.lifeExpectancy) {
            val currentYear = yearIndex + 1

            val width = if (currentYear < life.age) {
                canvasWidth
            } else {
                canvasWidth * (life.currentYearSpentWeeks.toFloat() / Life.totalWeeksInAYear.toFloat())
            }

            val color = if (currentYear < life.age) {
                AgeGroup.getGroupForAge(currentYear).getColor()
            } else if (currentYear == life.age) {
                AgeGroup.getGroupForAge(currentYear).getColor()
            } else {
                Color.Gray
            }

            drawRect(
                color = color,
                topLeft = Offset(
                    x = 0f,
                    y = yearIndex * cellHeight
                ),
                size = Size(
                    width = width,
                    // By adding extra height, we get rid of empty lines caused by
                    // inaccuracies in drawing (offset .. etc).
                    height = cellHeight + 1
                ),
            )
        }
    }
}

@Preview
@Composable
fun SimplifiedLifeCalendarPreview() {
    LifeProgressTheme {
        SimplifiedLifeCalendar(life = Life.example)
    }
}