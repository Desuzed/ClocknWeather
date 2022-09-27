package com.desuzed.everyweather.presentation.features.locatation_map

import com.google.android.gms.maps.model.LatLng

sealed interface MapUserInteraction {
    class NewLocationPicked(val location: LatLng) : MapUserInteraction
    object NewLocationConfirm : MapUserInteraction
    object DismissDialog : MapUserInteraction
}