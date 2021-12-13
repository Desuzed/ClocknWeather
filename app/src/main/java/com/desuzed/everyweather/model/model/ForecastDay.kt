package com.desuzed.everyweather.model.model

class ForecastDay(
    val date: String,

    val dateEpoch: Long,

    val day: Day,

    val astro: Astro,

    val hourForecast : List<Hour>,

    var isExpanded: Boolean = false
) {

}