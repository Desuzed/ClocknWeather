package com.desuzed.everyweather.view.fragments.weather.main

import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.ui.main.WeatherMainUi

data class WeatherState(
    val weatherData: WeatherResponse? = null,
    val weatherUi: WeatherMainUi? = null,
    val isAddButtonEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val query: String = "",
    val infoMessage: String = "",
)