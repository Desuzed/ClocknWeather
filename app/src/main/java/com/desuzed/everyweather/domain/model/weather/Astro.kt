package com.desuzed.everyweather.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Astro(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
)