package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun WorkInProgressCard(
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
    }

    Surface(
        modifier = modifier.padding(32.dp),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                imageVector = Icons.Default.Build,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "More features soon",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Application is in production",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Please be patient as this is not the final version of the app." +
                        "\n\n ･ﾟ･(●´Д｀●)･ﾟ･",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkInProgressCardPreview() {
    LifeProgressTheme {
        WorkInProgressCard()
    }
}