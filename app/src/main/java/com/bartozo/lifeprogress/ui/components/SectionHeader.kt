package com.bartozo.lifeprogress.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    section: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    val icon = when (isExpanded) {
        true -> Icons.Filled.KeyboardArrowDown
        false -> Icons.Filled.KeyboardArrowRight
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = titleStyle
            )
            Icon(
                imageVector = icon,
                contentDescription = "Arrow",
                tint = iconColor
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            section()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionHeaderPreview() {
    LifeProgressTheme {
        SectionHeader(
            title = "Learn more",
            section = { Text("This is text section") }
        )
    }
}