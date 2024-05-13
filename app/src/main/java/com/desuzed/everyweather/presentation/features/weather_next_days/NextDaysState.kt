package com.desuzed.everyweather.presentation.features.weather_next_days

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent

data class NextDaysState(
    val weather: WeatherContent? = null,
    val isExpanded: Boolean = false,
    val windSpeed: DistanceDimen = DistanceDimen.METRIC_KMH,
    val temperature: TempDimen = TempDimen.CELCIUS,
    val pressure: PressureDimen = PressureDimen.MILLIMETERS,
    val selectedLang: Lang = Lang.EN,
)