package com.desuzed.everyweather.presentation.features.locatation_map

sealed interface MapAction {
    class NavigateToWeather(val query: String) : MapAction
}