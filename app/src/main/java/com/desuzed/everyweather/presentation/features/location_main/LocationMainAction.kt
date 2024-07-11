package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.QueryResult

sealed interface LocationMainAction {
    class ShowSnackbar(val queryResult: QueryResult) : LocationMainAction
    class NavigateToWeather(val query: String) : LocationMainAction
    class NavigateToWeatherWithLatLng(val latLng: UserLatLng) : LocationMainAction
    data object MyLocation : LocationMainAction
    class ToggleMap(val isVisible: Boolean) : LocationMainAction
    data object NavigateToSettings : LocationMainAction
    data object NavigateBack : LocationMainAction
    data object RequestLocationPermissions : LocationMainAction
}