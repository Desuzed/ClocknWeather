package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.data.repository.providers.action_result.QueryResult

sealed interface LocationMainAction {
    class ShowSnackbar(val queryResult: QueryResult) : LocationMainAction
    class NavigateToWeather(val query: String, val key: String) : LocationMainAction
    object MyLocation : LocationMainAction
    object ShowMapFragment : LocationMainAction
    object NavigateToSettings : LocationMainAction
    object NavigateBack : LocationMainAction
    object RequestLocationPermissions : LocationMainAction
}