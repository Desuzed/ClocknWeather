package com.desuzed.everyweather.domain.model.weather

class Day(
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

    val popSnow: Int,

    val text: String,

    val icon: String
) {

    // var pop : Int = (popRain + popSnow)/2
}