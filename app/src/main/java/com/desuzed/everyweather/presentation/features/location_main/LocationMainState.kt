package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.geo.GeoData

data class LocationMainState(
    val geoText: String = "",
    val editLocationText: String = "",
    val locations: List<FavoriteLocation> = emptyList(),
    val geoData: List<GeoData>? = null,
    val isLoading: Boolean = false,
    val showPickerDialog: Boolean = false,
    val showEditLocationDialog: FavoriteLocation? = null,
    val showRequireLocationPermissionsDialog: Boolean = false,
)
