package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.presentation.base.Action

sealed interface WeatherAction : Action {
    data object Location : WeatherAction
    data object SaveLocation : WeatherAction
    data object Refresh : WeatherAction
    data object Redirection : WeatherAction
}