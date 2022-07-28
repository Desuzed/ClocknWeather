package com.desuzed.everyweather.domain.model

class Day(
    val maxTemp : Float,

    val minTemp : Float,

    val maxWind : Float,

    val totalPrecip : Float,

    val avgHumidity : Float,

    val popRain: Int,

    val popSnow: Int ,

    val text: String,

    val icon: String
) {

    // var pop : Int = (popRain + popSnow)/2
}