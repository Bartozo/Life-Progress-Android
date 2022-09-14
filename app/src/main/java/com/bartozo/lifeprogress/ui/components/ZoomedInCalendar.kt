package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.data.AgeGroup
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun ZoomedInCalendar(
    modifier: Modifier = Modifier,
    columns: Int = 3,
    rows: Int = 3
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.background)
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                shape = CircleShape
            )
    ) {
        Column {
            Row(modifier = Modifier.weight(1f)) { }
            Row(modifier = Modifier.weight(1f)) {
                Spacer(modifier = Modifier.weight(1f))
                Canvas(modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                ) {
                    val canvasWidth = size.width
                    val cellSize = canvasWidth / rows
                    val cellPadding = cellSize / 12

                    for (rowIndex in 0..rows) {
                        for (columnIndex in 0..columns) {
                            val cellOffset = Offset(
                                x = columnIndex * cellSize + cellPadding,
                                y = rowIndex * cellSize + cellPadding
                            )
                            val color = if (rowIndex < 2) {
                                AgeGroup.BABY.getColor()
                            } else {
                                AgeGroup.CHILD.getColor()
                            }
                            val alpha = if ((rowIndex == 0) && (columnIndex == 0)) {
                                color.alpha
                            } else {
                                0.2f
                            }

                            drawRect(
                                color = color.copy(alpha = alpha),
                                topLeft = cellOffset,
                                size = Size(
                                    width = cellSize - cellPadding * 2,
                                    height = cellSize - cellPadding * 2
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ZoomedInCalendarPreview() {
    LifeProgressTheme {
        ZoomedInCalendar(modifier = Modifier.size(200.dp))
    }
}