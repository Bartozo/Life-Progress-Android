package com.bartozo.lifeprogress.data

enum class AppTheme {
    SYSTEM_AUTO,
    LIGHT,
    DARK;

    fun getLocalizedString(): String {
        return when (this) {
            SYSTEM_AUTO -> "System Auto"
            LIGHT -> "Default"
            DARK -> "Default Dark"
        }
    }
}

val appThemes = listOf(
    AppTheme.SYSTEM_AUTO,
    AppTheme.LIGHT,
    AppTheme.DARK
)