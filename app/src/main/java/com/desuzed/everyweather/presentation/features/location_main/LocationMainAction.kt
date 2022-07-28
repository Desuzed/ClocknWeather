package com.desuzed.everyweather.presentation.features.location_main


sealed interface LocationMainAction{
    class ShowToast(val message: String) : LocationMainAction
    class NavigateToWeather(val query: String, val key: String): LocationMainAction
    object MyLocation: LocationMainAction
    object ShowMapFragment: LocationMainAction
}