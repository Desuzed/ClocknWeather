package com.desuzed.everyweather.view.fragments.location.map

import com.desuzed.everyweather.model.entity.Location
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val location: Location? = null,
    val newPickedLocation: LatLng? = null,
    val shouldShowDialog: Boolean = false,
    val loadNewLocationWeather: Boolean = false,
)
