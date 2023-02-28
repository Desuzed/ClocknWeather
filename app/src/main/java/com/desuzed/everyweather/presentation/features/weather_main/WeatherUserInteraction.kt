package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface WeatherUserInteraction : UserInteraction {
    object Location : WeatherUserInteraction
    object NextDays : WeatherUserInteraction
    object SaveLocation : WeatherUserInteraction
    object Refresh : WeatherUserInteraction
    object Redirection : WeatherUserInteraction
}