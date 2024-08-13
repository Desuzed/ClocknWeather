package com.desuzed.everyweather.ui.theming

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class EveryweatherColors(
    val primary: Color,
    val primaryInvariant: Color,
    val primaryBackground: List<Color>,
    val secondaryBackground: List<Color>,
    val tertiaryBackground: Color,
    val errorBackground: Color,
    val surfacePrimary: Color,
    val surfaceSecondary: Color,//todo delete
    val surfaceOnPrimaryBg: Color,
    val surfaceOnSecondaryBg: Color = Gray30Alpha,
    val onBackgroundPrimary: Color,
    val onBackgroundSecondary: Color,
    val onBackgroundInvariant: Color,
    val onErrorBackground: Color,
    val onPrimary: Color,
    val neutral: Color,
    val accent: Color,
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
val LightAlertRed = Color(0xFFE04844)
val DarkAlertRed = Color(0xFFA61A1A)

// Gradient
val BackgroundGradientLightStart = LightBlue50customized
val BackgroundGradientLightEnd = Color(0xFF00C4DE)
val BackgroundGradientNightStart = Indigo700customized
val BackgroundGradientNightEnd = Color(0xFF011131)
val PrimaryGradientNightStart = Color(0xFF000716)
val PrimaryGradientNightEnd = Color(0xFF000E2E)

val LightColorPalette = EveryweatherColors(
    primary = indigo700,
    primaryInvariant = lightBlue700,
    surfacePrimary = Blue50DarkCustomized,
    surfaceSecondary = Blue50customized,
    onBackgroundPrimary = NearDark,
    onBackgroundSecondary = White,
    onBackgroundInvariant = White,
    onErrorBackground = White,
    onPrimary = NearWhite,
    neutral = Gray,
    primaryBackground = listOf(White, White),
    secondaryBackground = listOf(BackgroundGradientLightStart, BackgroundGradientLightEnd),
    tertiaryBackground = White,
    errorBackground = LightAlertRed,
    accent = Indigo700customized,
    surfaceOnPrimaryBg = White,
)

val DarkColorPalette = EveryweatherColors(
    primary = lightBlue700,
    primaryInvariant = indigo700,
    surfacePrimary = Indigo400DarkCustomized,
    surfaceSecondary = Indigo300DarkCustomized,
    onBackgroundPrimary = NearWhite,
    onBackgroundSecondary = NearWhite,
    onBackgroundInvariant = NearDark,
    onErrorBackground = NearWhite,
    onPrimary = NearWhite,
    neutral = Gray,
    primaryBackground = listOf(PrimaryGradientNightStart, PrimaryGradientNightEnd),
    secondaryBackground = listOf(BackgroundGradientNightStart, BackgroundGradientNightEnd),
    tertiaryBackground = AlmostBlack,
    errorBackground = DarkAlertRed,
    accent = LightBlue50customized,
    surfaceOnPrimaryBg = Indigo300DarkCustomized,
)

val LocalAppColors = staticCompositionLocalOf<EveryweatherColors> {
    error("No colors provided")
}