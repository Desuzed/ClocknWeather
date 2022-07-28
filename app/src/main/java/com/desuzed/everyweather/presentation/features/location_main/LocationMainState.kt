package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.data.room.FavoriteLocationDto

data class LocationMainState(
    val geoText: String = "",
    val locations: List<FavoriteLocationDto> = emptyList(),
)
