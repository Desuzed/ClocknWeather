package com.desuzed.everyweather.presentation.features.weather_main

sealed interface WeatherUserInteraction {
    object Location : WeatherUserInteraction
    object NextDays : WeatherUserInteraction
    object SaveLocation : WeatherUserInteraction
    object Refresh : WeatherUserInteraction
    class Redirection(val message: String) : WeatherUserInteraction
}