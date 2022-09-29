package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.data.repository.providers.action_result.QueryResult

sealed interface WeatherMainAction {
    class ShowSnackbar(val queryResult: QueryResult) : WeatherMainAction
    object NavigateToLocation : WeatherMainAction
    object NavigateToNextDaysWeather : WeatherMainAction
}