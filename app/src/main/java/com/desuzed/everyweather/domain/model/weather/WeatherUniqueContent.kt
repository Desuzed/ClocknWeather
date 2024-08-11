package com.desuzed.everyweather.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherUniqueContent(
    val id: String,
    val content: WeatherContent,
)
