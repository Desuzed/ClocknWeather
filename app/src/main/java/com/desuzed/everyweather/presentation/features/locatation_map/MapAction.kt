package com.desuzed.everyweather.presentation.features.locatation_map

import com.desuzed.everyweather.domain.model.location.UserLatLng

sealed interface MapAction {
    class NavigateToWeather(val latLng: UserLatLng) : MapAction
}