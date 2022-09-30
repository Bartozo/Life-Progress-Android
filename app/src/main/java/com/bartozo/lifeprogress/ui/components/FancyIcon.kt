package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun FancyIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    iconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {
    val containerAlignments = listOf(
        Alignment.TopStart, Alignment.TopEnd,
        Alignment.BottomStart, Alignment.BottomEnd
    )

    Box(modifier = modifier.size(30.dp)) {
        for (i in 0..3) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(containerColor)
                    .align(containerAlignments[i])
            )
        }
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor
        )
    }
}

@Preview
@Composable
private fun FancyIconPreview() {
    LifeProgressTheme {
        FancyIcon(
            icon = Icons.Default.NorthEast,
            contentDescription = "icon"
        )
    }
}