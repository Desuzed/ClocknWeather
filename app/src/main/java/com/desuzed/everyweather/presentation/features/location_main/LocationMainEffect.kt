package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.presentation.base.SideEffect
import com.desuzed.everyweather.presentation.base.SnackBarEffect

sealed interface LocationMainEffect : SideEffect {
    class ShowSnackbar(override val data: QueryResult) : LocationMainEffect, SnackBarEffect
    class NavigateToWeather(val query: String) : LocationMainEffect
    class NavigateToWeatherWithLatLng(val latLng: UserLatLng) : LocationMainEffect
    data object MyLocation : LocationMainEffect
    class ToggleMap(val isVisible: Boolean) : LocationMainEffect
    data object NavigateToSettings : LocationMainEffect
    data object NavigateBack : LocationMainEffect
    data object RequestLocationPermissions : LocationMainEffect
}