package com.desuzed.everyweather.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    val timeEpoch: Long,
    val tempC: Float,
    val tempF: Float,
    val text: String,
    val icon: String,
    val windSpeedKmh: Float,
    val windSpeedMph: Float,
    val windDegree: Int,
    val pressureMb: Float,
    val pressureInch: Float,
)