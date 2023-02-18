package com.bartozo.lifeprogress.ui.components.welcome


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun PrimaryAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.filledTonalShape,
    colors: PrimaryActionColors = PrimaryActionDefaults.primaryActionColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            contentColor = colors.contentColor().value,
            containerColor = colors.containerColor().value
        ),
        contentPadding = contentPadding,
        content = content
    )
}
object PrimaryActionDefaults {

    /**
     * Creates a [ButtonColors] that represents the default container and content colors used in a
     * [Button].
     *
     * @param containerColor the container color of this [Button] when enabled.
     * @param contentColor the content color of this [Button] when enabled.
     */
    @Composable
    fun primaryActionColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary
    ): PrimaryActionColors = PrimaryActionColors(
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@Immutable
class PrimaryActionColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color
) {
    /**
     * Represents the container color for this button.
     */
    @Composable
    internal fun containerColor(): State<Color> {
        return rememberUpdatedState(containerColor)
    }

    /**
     * Represents the content color for this button.
     */
    @Composable
    internal fun contentColor(): State<Color> {
        return rememberUpdatedState(contentColor)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is PrimaryActionColors) return false

        if (containerColor != other.containerColor) return false
        if (contentColor != other.contentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + contentColor.hashCode()

        return result
    }
}