package com.desuzed.everyweather.view.fragments.weather.main

sealed interface WeatherMainAction {
    class ShowToast(val message: String) : WeatherMainAction
}