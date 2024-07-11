package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.geo.GeoData
import com.desuzed.everyweather.domain.model.weather.Location

data class LocationMainState(
    val geoText: String = "",
    val editLocationText: String = "",
    val locations: List<FavoriteLocation> = emptyList(),
    val geoData: List<GeoData>? = null,
    val isLoading: Boolean = false,

    val mapPinLocation: Location? = null,
    val newPickedLocation: UserLatLng? = null,
    val loadNewLocationWeather: Boolean = false,
    //TODO: Перенести
    val locationDialog: LocationDialog? = null,
)
