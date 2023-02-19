package com.bartozo.lifeprogress.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bartozo.lifeprogress.R
import com.bartozo.lifeprogress.ui.theme.LifeProgressTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LifeExpectancyCard(
    modifier: Modifier = Modifier,
    lifeExpectancy: Int,
    onLifeExpectancySelect: (Int) -> Unit
) {
    var sliderPosition by remember { mutableStateOf(lifeExpectancy.toFloat()) }

    LaunchedEffect(key1 = lifeExpectancy) {
        sliderPosition = lifeExpectancy.toFloat()
    }

    ListItem(
        modifier = modifier,
        headlineText = {
            Text(
                text = stringResource(id = R.string.life_expectancy),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Face,
                contentDescription = "Face Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            AnimatedContent(
                targetState = lifeExpectancy,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) { targetCount ->
                Text(
                    modifier = Modifier.width(25.dp),
                    text = "$targetCount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        supportingText = {
            Slider(
                modifier = Modifier.offset(
                    x = (-8).dp
                ),
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    onLifeExpectancySelect(it.roundToInt())
                },
                valueRange = 18f..150f,
                onValueChangeFinished = {
                    onLifeExpectancySelect(sliderPosition.roundToInt())
                },
            )
        }
    )
}

@Preview
@Composable
private fun LifeExpectancyCard() {
    LifeProgressTheme {
        LifeExpectancyCard(
            lifeExpectancy = 90,
            onLifeExpectancySelect = {}
        )
    }
}


