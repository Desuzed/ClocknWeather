package com.desuzed.everyweather.presentation.features.weather_next_days.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMainInfo
import com.desuzed.everyweather.ui.AppPreview
import com.desuzed.everyweather.ui.elements.BoldText
import com.desuzed.everyweather.ui.elements.RegularText
import com.desuzed.everyweather.ui.theming.EveryweatherTheme
import com.desuzed.everyweather.util.MockWeatherObject
import java.util.TimeZone

@AppPreview
@Composable
private fun Preview() {
    EveryweatherTheme {
        NextDayItemContent(
            nextDaysMainInfo = NextDaysMainInfo(
                language = Lang.RU,
                temperature = TempDimen.CELCIUS,
                forecastDay = MockWeatherObject.forecastDay,
                timeZone = TimeZone.getDefault().toString(),
                res = LocalContext.current.resources,
            )
        )
    }
}

@Composable
fun NextDayItemContent(nextDaysMainInfo: NextDaysMainInfo) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_20),
                vertical = dimensionResource(id = R.dimen.dimen_10),
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dimen_8)),
        ) {
            BoldText(text = nextDaysMainInfo.date)
            RegularText(text = nextDaysMainInfo.description, maxLines = 1)
        }
        MaxMinTempWithImg(nextDaysMainInfo = nextDaysMainInfo)
    }
}