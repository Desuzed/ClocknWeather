package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.base.SideEffect

sealed interface WeatherMainEffect : SideEffect {
    class ShowSnackbar(val queryResult: QueryResult) : WeatherMainEffect
    data object NavigateToLocation : WeatherMainEffect
    data object NavigateToNextDaysWeather : WeatherMainEffect
}