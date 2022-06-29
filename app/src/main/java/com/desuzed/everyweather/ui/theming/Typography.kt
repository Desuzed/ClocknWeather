package com.desuzed.everyweather.ui.theming

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.desuzed.everyweather.R

private val montserratRegular = FontFamily(
    Font(R.font.montserrat_regular_ru, FontWeight.Black)
)

private val montserratRegularBold = FontFamily(
    Font(R.font.montserrat_semi_bold, FontWeight.Black)
)


data class EveryweatherTypography(
    val h3: TextStyle = TextStyle(
        fontFamily = montserratRegularBold,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
    ),
    val textLarge: TextStyle = TextStyle(
        fontFamily = montserratRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 66.sp,
        lineHeight = 25.sp,
    ),
    val textMedium: TextStyle = TextStyle(
        fontFamily = montserratRegular,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 23.sp,
    ),
    val textMediumBold: TextStyle = TextStyle(
        fontFamily = montserratRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 23.sp,
    ),
    val text: TextStyle = TextStyle(
        fontFamily = montserratRegular,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    val textSmall: TextStyle = TextStyle(
        fontFamily = montserratRegular,
        fontWeight = FontWeight.Medium, //todo поиграться поменять
        fontSize = 12.sp,
    ),
)

val LocalAppTypography = staticCompositionLocalOf<EveryweatherTypography> {
    error("No typography provided")
}
