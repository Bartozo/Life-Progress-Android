package com.bartozo.lifeprogress.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    headline: String,
    supportingText: String,
    header: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        ),
        shape = RoundedCornerShape(size = 28.dp)
    ) {
        Column(
            modifier = Modifier.clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
        ) {
            if (header != null) {
                 Box(
                     modifier = Modifier
                         .fillMaxWidth()
                         .height(188.dp)
                         .clip(RoundedCornerShape(size = 28.dp))
                         .background(MaterialTheme.colorScheme.tertiaryContainer)
                 ) {
                     header()
                 }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp),
                        text = headline,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (onClick != null) {
                        InfoIcon()
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = supportingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoIcon(
    modifier: Modifier = Modifier
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
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .align(containerAlignments[i])
            )
        }
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.Default.NorthEast,
            contentDescription = "Arrow Icon",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
private fun InformationCardPreview() {
    LifeProgressTheme {
        InformationCard(
            headline = "A calendar for your life",
            supportingText = "Each square you see on screen represents a week in your life." +
                    "The first square (the one at the top left) is the week you were born.",
        )
    }
}

@Preview
@Composable
private fun InformationCardWithHeaderPreview() {
    LifeProgressTheme {
        InformationCard(
            headline = "A calendar for your life",
            supportingText = "Each square you see on screen represents a week in your life." +
                    "The first square (the one at the top left) is the week you were born.",
            header = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f)) {
                        SimplifiedLifeCalendar(
                            modifier = Modifier
                                .align(alignment = Alignment.BottomCenter)
                                .offset(x = 50.dp, y = 50.dp),
                            life = Life.example
                        )
                        ZoomedInCalendar(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .height(75.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        )
    }
}

@Preview
@Composable
private fun ClickableInformationCardPreview() {
    LifeProgressTheme {
        InformationCard(
            headline = "Your Life in Weeks",
            supportingText = "This idea was originally introduced in an article by Tim Urban.",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun InfoIconPreview() {
    LifeProgressTheme {
        InfoIcon()
    }
}