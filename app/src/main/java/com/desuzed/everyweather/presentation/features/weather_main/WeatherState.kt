package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.presentation.base.State
import com.desuzed.everyweather.util.Constants.EMPTY_STRING

data class WeatherState(
    val weatherData: WeatherContent? = null,
    val isAddButtonEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val query: String = EMPTY_STRING,
    val selectedLang: Lang = Lang.EN,
    val windSpeed: DistanceDimen = DistanceDimen.METRIC_KMH,
    val temperature: TempDimen = TempDimen.CELCIUS,
    val pressure: PressureDimen = PressureDimen.MILLIMETERS,
) : State