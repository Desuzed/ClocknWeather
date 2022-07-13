package com.desuzed.everyweather.ui.theming

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class EveryweatherColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val onSurface: Color,
    val textColorPrimary: Color,
    val textColorSecondary: Color,
    val buttonTextColor: Color,
    val editTextStrokeColor: Color,
    val primaryGradientStart: Color,
    val primaryGradientEnd: Color,
    val secondaryGradientStart: Color,
    val secondaryGradientEnd: Color,
    val bottomDialogBackground: Color,
    val textBg: Color = Gray30Alpha,
)

//MaterialColors
val indigo700 = Color(0xFF303F9F)
val lightBlue700 = Color(0xFF0277BD)


// Customized material colors
val LightBlue50customized = Color(0xFF8DDFEA)
val Indigo700customized = Color(0xFF203C85)
val Blue50customized = Color(0xFFDEECFF)
val Indigo300DarkCustomized = Color(0xFF334766)
val Blue50DarkCustomized = Color(0xFFEFF6FF)
val Indigo400DarkCustomized = Color(0xFF3C567D)
val NearDark = Color(0xFF4D4D4D)
val NearWhite = Color(0xFFD6D6D6)
val AlmostBlack = Color(0xFF121212)
val White = Color(0xFFFFFFFF)
val Gray = Color(0x99999999)
val Gray30Alpha = Color(0x4DFFFFFF)

// Gradient
val BackgroundGradientLightStart = LightBlue50customized
val BackgroundGradientLightEnd = Color(0xFF00C4DE)
val BackgroundGradientNightStart = Indigo700customized
val BackgroundGradientNightEnd = Color(0xFF011131)
val SecondaryGradientNightStart = Color(0xFF000716)
val SecondaryGradientNightEnd = Color(0xFF000E2E)

val LightColorPalette = EveryweatherColors(
    primary = LightBlue50customized,
    secondary = indigo700,
    tertiary = Blue50customized,
    onSurface = Blue50DarkCustomized,
    textColorPrimary = NearDark,
    textColorSecondary = White,
    buttonTextColor = NearWhite,
    editTextStrokeColor = Gray,
    primaryGradientStart = BackgroundGradientLightStart,
    primaryGradientEnd = BackgroundGradientLightEnd,
    secondaryGradientStart = White,
    secondaryGradientEnd = White,
    bottomDialogBackground = White,
)

val DarkColorPalette = EveryweatherColors(
    primary = Indigo700customized,
    secondary = lightBlue700,
    tertiary = Indigo300DarkCustomized,
    onSurface = Indigo400DarkCustomized,
    textColorPrimary = NearWhite,
    textColorSecondary = NearWhite,
    buttonTextColor = NearWhite,
    editTextStrokeColor = Gray,
    primaryGradientStart = BackgroundGradientNightStart,
    primaryGradientEnd = BackgroundGradientNightEnd,
    secondaryGradientStart = SecondaryGradientNightStart,
    secondaryGradientEnd = SecondaryGradientNightEnd,
    bottomDialogBackground = AlmostBlack,
)

val LocalAppColors = staticCompositionLocalOf<EveryweatherColors> {
    error("No colors provided")
}