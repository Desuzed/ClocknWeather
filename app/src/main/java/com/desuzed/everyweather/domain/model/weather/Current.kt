package com.desuzed.everyweather.domain.model.weather

data class Current(
    val tempC: Float,

    val tempF: Float,

    val text: String,

    val icon: String,

    val windSpeedKph: Float,

    val windSpeedMph: Float,

    val pressureMb: Float,

    val pressureInch: Float,

    val precipMm: Float,

    val precipInch: Float,

    val humidity: Int,

    val feelsLikeC: Float,

    val feelsLikeF: Float
)