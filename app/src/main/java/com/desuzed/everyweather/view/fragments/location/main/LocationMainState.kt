package com.desuzed.everyweather.view.fragments.location.main

import com.desuzed.everyweather.data.room.FavoriteLocationDto

data class LocationMainState(
    val geoText: String = "",
    val locations: List<FavoriteLocationDto> = emptyList(),
)
