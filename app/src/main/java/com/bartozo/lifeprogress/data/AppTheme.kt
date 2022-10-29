package com.bartozo.lifeprogress.data

import androidx.annotation.StringRes
import com.bartozo.lifeprogress.R

enum class AppTheme {
    SYSTEM_AUTO,
    LIGHT,
    DARK;

    @StringRes
    fun getLocalizedString(): Int {
        return when (this) {
            SYSTEM_AUTO -> R.string.system_auto_theme
            LIGHT -> R.string.light_theme
            DARK -> R.string.dark_theme
        }
    }
}

val appThemes = listOf(
    AppTheme.SYSTEM_AUTO,
    AppTheme.LIGHT,
    AppTheme.DARK
)