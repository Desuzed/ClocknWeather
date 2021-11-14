package com.desuzed.everyweather.mvvm.model

class ForecastDay(
    val date: String,

    val dateEpoch: Long,

    val day: Day,

    val astro: Astro,

    val hourForecast : ArrayList<Hour>,

    var isExpanded: Boolean = false
) {

}