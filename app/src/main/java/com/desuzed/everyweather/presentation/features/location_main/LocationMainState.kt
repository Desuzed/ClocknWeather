package com.desuzed.everyweather.presentation.features.location_main

import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse
import com.desuzed.everyweather.domain.model.settings.Language

data class LocationMainState(
    val geoText: String = "",
    val locations: List<FavoriteLocationDto> = emptyList(),
    val lang: Language = Language("", 0, 0),
    val geoResponses: List<GeoResponse>? = null,
    val isLoading: Boolean = false,
    val showPickerDialog: Boolean = false,
)
