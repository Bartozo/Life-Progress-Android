package com.bartozo.lifeprogress.ui.components.welcome

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class Feature(
    val title: String,
    val subtitle: String,
    val image: ImageVector
)