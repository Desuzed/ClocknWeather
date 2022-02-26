package com.desuzed.everyweather.model.model

data class Hour(
    val timeEpoch: Long,

    val temp: Float,

    val text: String,

    val icon: String,

    val windSpeed: Float,

    val windDegree: Int,

    val pressureMb: Float
)