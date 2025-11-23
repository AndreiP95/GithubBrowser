package com.andrei.githubbrowser.ui.base.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.andrei.githubbrowser.ui.base.*

internal val LightColors = lightColorScheme(
    primary = mdThemePrimary,
    onPrimary = mdThemeOnPrimary,
    primaryContainer = mdThemePrimaryContainer,
    onPrimaryContainer = mdThemeOnPrimaryContainer,
    background = mdThemeBackground,
    onBackground = mdThemeOnBackground,
    surface = mdThemeSurface,
    onSurface = mdThemeOnSurface,
    error = mdThemeError,
    onError = mdThemeOnError
)
internal val DarkColors = darkColorScheme(
    primary = mdThemeDarkPrimary,
    onPrimary = mdThemeDarkOnPrimary,
    primaryContainer = mdThemeDarkPrimaryContainer,
    onPrimaryContainer = mdThemeDarkOnPrimaryContainer,
    background = mdThemeDarkBackground,
    onBackground = mdThemeDarkOnBackground,
    surface = mdThemeDarkSurface,
    onSurface = mdThemeDarkOnSurface,
    error = mdThemeDarkError,
    onError = mdThemeDarkOnError
)
