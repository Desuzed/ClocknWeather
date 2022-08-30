package com.desuzed.everyweather.domain.model

class Current(
    val tempC: Float,

    val tempF: Float,

    val text: String,

    val icon: String,

    val windSpeedKph: Float,

    val windSpeedMph: Float,

    val windDegree: Int,

    val windDir: String,

    val pressureMb: Float,

    val precipMm: Float,

    val precipInch: Float,

    val humidity: Int,

    val feelsLikeC: Float,

    val feelsLikeF: Float
)