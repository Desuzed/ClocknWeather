package com.desuzed.everyweather.ui.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.desuzed.everyweather.ui.theming.DarkColorPalette
import com.desuzed.everyweather.ui.theming.EveryweatherTypography
import com.desuzed.everyweather.ui.theming.LightColorPalette

//object RobokotTheme {
//
//    val colors: RobokotColors
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalRobokotColors.current
//
//    val typography: RobokotTypography
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalRobokotTypography.current
//}
//
//@Suppress("UNUSED_PARAMETER")
//@Composable
//fun RobokotTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
//) {
//
//    val colors = RobokotColorPalette
//    val typography = RobokotTypography()
//
//    CompositionLocalProvider(
//        LocalRobokotColors provides colors,
//        LocalRobokotTypography provides typography,
//        content = content,
//    )
//}

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



