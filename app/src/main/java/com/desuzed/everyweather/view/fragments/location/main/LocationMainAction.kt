package com.desuzed.everyweather.view.fragments.location.main


sealed interface LocationMainAction{
    class ShowToast(val message: String) : LocationMainAction
    class NavigateToWeather(val query: String, val key: String): LocationMainAction
}