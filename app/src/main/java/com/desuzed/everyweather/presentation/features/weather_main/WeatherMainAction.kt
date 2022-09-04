package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.domain.model.ActionResult

sealed interface WeatherMainAction {
    class ShowSnackbar(val actionResult: ActionResult) : WeatherMainAction
    object NavigateToLocation : WeatherMainAction
    object NavigateToNextDaysWeather : WeatherMainAction
}