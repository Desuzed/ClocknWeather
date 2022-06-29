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
    val gradientStart: Color,
    val gradientEnd: Color,
    val textBg: Color = Color(0x4DFFFFFF), //todo пеереименовать
)

//<color name="edit_text_bg_light">#99999999</color>
//<color name="edit_text_bg_night">#99E6E6E6</color>
//<color name="place_background">#4DFFFFFF</color>
//<color name="bgDetailLight">#EFF6FF</color>
//<color name="bgDetailNight">#3C567D</color>

//<color name="bg_black_night">#121212</color>
//<color name="bg_white_light">#FFFFFF</color>
//<color name="errorLight">#E04844</color>
//<color name="errorNight">#A61A1A</color>
//<color name="textLight">#4D4D4D</color>
//<color name="textNight">#D6D6D6</color>
//<color name="primary">#42A5F5</color>
//<color name="secondary">#448AFF</color>
//<color name="transparent">#00FBFBFB</color>


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
val White = Color(0xFFFFFFFF)

//<color name="bgDetailLight">#EFF6FF</color>
//<color name="bgDetailNight">#3C567D</color>

// Gradient
val BackgroundGradientLightStart = LightBlue50customized
val BackgroundGradientLightEnd = Color(0xFF00C4DE)
val BackgroundGradientNightStart = Indigo700customized
val BackgroundGradientNightEnd = Color(0xFF011131)


// Alpha
val LoaderBackground = Color(0x265646D1)

val LightColorPalette = EveryweatherColors(
    primary = LightBlue50customized,
    secondary = indigo700,
    tertiary = Blue50customized,
    onSurface = Blue50DarkCustomized,
    textColorPrimary = NearDark,
    textColorSecondary = White,
    gradientStart = BackgroundGradientLightStart,
    gradientEnd = BackgroundGradientLightEnd,
    )

val DarkColorPalette = EveryweatherColors(
    primary = Indigo700customized,
    secondary = lightBlue700,
    tertiary = Indigo300DarkCustomized,
    onSurface = Indigo400DarkCustomized,
    textColorPrimary = NearWhite,
    textColorSecondary = NearWhite,
    gradientStart = BackgroundGradientNightStart,
    gradientEnd = BackgroundGradientNightEnd,
    )

val LocalAppColors = staticCompositionLocalOf<EveryweatherColors> {
    error("No colors provided")
}