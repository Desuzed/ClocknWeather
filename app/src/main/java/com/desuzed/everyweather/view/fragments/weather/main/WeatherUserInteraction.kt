package com.desuzed.everyweather.view.fragments.weather.main

sealed interface WeatherUserInteraction {
    object Location : WeatherUserInteraction
    object NextDays : WeatherUserInteraction
    object SaveLocation : WeatherUserInteraction
    object Refresh : WeatherUserInteraction
}