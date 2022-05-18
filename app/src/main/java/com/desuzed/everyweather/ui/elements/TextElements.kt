package com.desuzed.everyweather.ui.elements

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun BoldText(text: String) {
    Text(
        text = text,
        style = EveryweatherTheme.typography.h3
    )
}

@Composable
fun RegularText(text: String) {
    Text(
        text = text,
        style = EveryweatherTheme.typography.text,
    )
}

@Composable
fun SmallText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = EveryweatherTheme.typography.textSmall,
        maxLines = 1,
    )
}