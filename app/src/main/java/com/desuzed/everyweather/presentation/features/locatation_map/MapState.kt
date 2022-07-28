package com.desuzed.everyweather.presentation.features.locatation_map

import com.desuzed.everyweather.domain.model.Location
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val location: Location? = null,
    val newPickedLocation: LatLng? = null,
    val shouldShowDialog: Boolean = false,
    val loadNewLocationWeather: Boolean = false,
)
