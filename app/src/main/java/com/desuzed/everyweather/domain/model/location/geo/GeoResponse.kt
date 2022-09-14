package com.desuzed.everyweather.domain.model.location.geo

data class GeoResponse(
    val lat: String,
    val lon: String,
    val name: String,
    val importance: Float,
)
