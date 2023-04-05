package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.QueryResult

sealed interface LocationMainAction {
    class ShowSnackbar(val queryResult: QueryResult) : LocationMainAction
    class NavigateToWeather(val query: String) : LocationMainAction
    class NavigateToWeatherWithLatLng(val latLng: UserLatLng) : LocationMainAction
    object MyLocation : LocationMainAction
    object ShowMapFragment : LocationMainAction
    object NavigateToSettings : LocationMainAction
    object NavigateBack : LocationMainAction
    object RequestLocationPermissions : LocationMainAction
}