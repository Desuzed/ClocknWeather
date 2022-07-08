package com.desuzed.everyweather.view.fragments.location.map

sealed interface MapAction {
    class NavigateToWeather(val query: String) : MapAction
}