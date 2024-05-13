package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_main.WeatherUserInteraction
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.GradientBox
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        EmptyWeatherCard {}
    }
}

@Composable
fun EmptyWeatherCard(
    modifier: Modifier = Modifier,
    onUserInteraction: (WeatherUserInteraction) -> Unit,
) {
    GradientBox(
        modifier = Modifier.fillMaxSize(),
        colors = EveryweatherTheme.colors.primaryBackground,
    ) {
        Card(
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.dimen_20))
                .align(Alignment.Center),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_16)),
            backgroundColor = EveryweatherTheme.colors.surfacePrimary,
            elevation = dimensionResource(id = R.dimen.dimen_4),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_20)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_20)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BoldText(
                    text = stringResource(id = R.string.no_weather_data_is_loaded_header),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                RegularText(text = stringResource(id = R.string.no_weather_data_is_loaded))
                RoundedButton(
                    onClick = { onUserInteraction(WeatherUserInteraction.Location) },
                    text = stringResource(id = R.string.choose_location)
                )
            }
        }
    }

}