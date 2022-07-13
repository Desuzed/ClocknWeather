package com.desuzed.everyweather.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun EveryweatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val typography = EveryweatherTypography()
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        content = content,
    )
}

object EveryweatherTheme {

    val colors: EveryweatherColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: EveryweatherTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}



