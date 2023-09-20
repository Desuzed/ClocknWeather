package com.desuzed.everyweather.presentation.features.weather_main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.presentation.features.weather_main.WeatherUserInteraction
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.ui.elements.CardDetailDayItem
import com.desuzed.everyweather.ui.elements.GradientBox
import com.desuzed.everyweather.ui.elements.HourItemContent
import com.desuzed.everyweather.ui.elements.LinkText
import com.desuzed.everyweather.ui.elements.RoundedButton
import com.desuzed.everyweather.ui.theming.EveryweatherTheme

@Composable
fun BottomDetailWeather(
    weatherUi: WeatherMainUi,
    onUserInteraction: (WeatherUserInteraction) -> Unit
) {
    GradientBox(
        colors = listOf(
            EveryweatherTheme.colors.secondaryGradientStart,
            EveryweatherTheme.colors.secondaryGradientEnd,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.dimen_20),
                    start = dimensionResource(id = R.dimen.dimen_20),
                    end = dimensionResource(id = R.dimen.dimen_20),
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_10)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //todo придумать анимацию
            LazyRow {
                items(items = weatherUi.hourList) { hourItem ->
                    HourItemContent(hourItem = hourItem)
                }
            }
            CardDetailDayItem(detailCard = weatherUi.detailCard)
            RoundedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onUserInteraction(WeatherUserInteraction.NextDays) },
                text = stringResource(id = R.string.next_days_forecast)
            )
            val inputText = stringResource(id = R.string.powered_by)
            val startIndex = inputText.indexOf(":") + 2
            LinkText(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = dimensionResource(id = R.dimen.dimen_20)),
                inputText = inputText,
                url = stringResource(id = R.string.uri),
                startIndex = startIndex,
                endIndex = startIndex + 11,
                onClick = { onUserInteraction(WeatherUserInteraction.Redirection) }
            )
        }
    }

}