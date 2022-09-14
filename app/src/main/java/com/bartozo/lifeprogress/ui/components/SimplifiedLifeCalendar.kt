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
        val progressWithoutCurrentYear = life.age / life.lifeExpectancy.toFloat()

        for (group in AgeGroup.values()) {
            val previousAgeGroupProportion = group.age / life.lifeExpectancy.toFloat()
            val color = if (life.age > group.age) {
                group.getColor()
            } else {
                Color.Gray
            }

            drawRect(
                color = color,
                topLeft = Offset(
                    y = canvasHeight * previousAgeGroupProportion,
                    x = 0f
                ),
                size = Size(
                    width = canvasWidth,
                    height = canvasHeight * progressWithoutCurrentYear
                )
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