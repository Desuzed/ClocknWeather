package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi

data class WeatherState(
    val weatherData: WeatherResponse? = null,
    val weatherUi: WeatherMainUi? = null,
    val isAddButtonEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val query: String = "",
    val language: Language = Language.RU,
 //   val infoMessage: String = "",
)