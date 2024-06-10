package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface WeatherUserInteraction : UserInteraction {
    data object Location : WeatherUserInteraction
    data object SaveLocation : WeatherUserInteraction
    data object Refresh : WeatherUserInteraction
    data object Redirection : WeatherUserInteraction
}