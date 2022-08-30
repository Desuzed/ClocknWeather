package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed

data class WeatherState(
    val weatherData: WeatherResponse? = null,
    val isAddButtonEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val query: String = "",
    val lang: Language = Language("", 0, 0),
    val windSpeed: WindSpeed = WindSpeed("", 0, 0),
    val temperature: Temperature = Temperature("", 0, 0),
    val pressure: Pressure = Pressure("", 0, 0),
)