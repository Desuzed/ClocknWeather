package com.desuzed.everyweather.presentation.features.locatation_map

import com.desuzed.everyweather.presentation.base.UserInteraction
import com.google.android.gms.maps.model.LatLng

sealed interface MapUserInteraction : UserInteraction {
    class NewLocationPicked(val location: LatLng) : MapUserInteraction
    object NewLocationConfirm : MapUserInteraction
    object DismissDialog : MapUserInteraction
}