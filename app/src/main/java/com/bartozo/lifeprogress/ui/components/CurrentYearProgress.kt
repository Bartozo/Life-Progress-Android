package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bartozo.lifeprogress.data.AgeGroup
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun CurrentYearProgress(
    modifier: Modifier = Modifier,
    life: Life
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val cellSize = canvasWidth / Life.totalWeeksInAYear
        val cellPadding = cellSize / 12

        for (weekIndex in 0 until Life.totalWeeksInAYear) {
            val cellOffset = Offset(
                x = weekIndex * cellSize + cellPadding,
                y = cellSize + cellPadding
            )
            val color = if (weekIndex < life.weekOfYear) {
                AgeGroup.getGroupForAge(life.age + 1).getColor()
            } else {
                Color.Gray
            }

            drawRect(
                color = color,
                topLeft = cellOffset,
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
fun CurrentYearProgressPreview() {
    LifeProgressTheme {
        CurrentYearProgress(
            life = Life.example
        )
    }
}