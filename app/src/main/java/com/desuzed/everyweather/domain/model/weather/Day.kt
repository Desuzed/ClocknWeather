package com.desuzed.everyweather.domain.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val maxTempC: Float,
    val maxTempF: Float,
    val minTempC: Float,
    val minTempF: Float,
    val maxWindKph: Float,
    val maxWindMph: Float,
    val totalPrecipMm: Float,
    val totalPrecipInch: Float,
    val avgHumidity: Float,
    val popRain: Int,
    val text: String,
    val icon: String,
)