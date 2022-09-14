package com.desuzed.everyweather.presentation.features.weather_next_days

import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherResponse

data class NextDaysState(
    val weather: WeatherResponse? = null,
    val isExpanded: Boolean = false,
    val windSpeed: WindSpeed = WindSpeed("", 0, 0),
    val temperature: Temperature = Temperature("", 0, 0),
    val language: Language = Language("", 0, 0),
    val pressure: Pressure = Pressure("", 0, 0),
)