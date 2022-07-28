package com.desuzed.everyweather.presentation.features.weather_main

sealed interface WeatherMainAction {
    class ShowToast(val message: String) : WeatherMainAction
    object NavigateToLocation : WeatherMainAction
    object NavigateToNextDaysWeather : WeatherMainAction
}