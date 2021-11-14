package com.desuzed.everyweather.mvvm.model

class Hour(
    val timeEpoch: Long,

    val temp: Float,

    val text: String,

    val icon: String,

    val windSpeed: Float,

    val windDegree: Int,

    val pressureMb: Float
) {


}