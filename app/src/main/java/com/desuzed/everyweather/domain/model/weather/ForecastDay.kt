package com.desuzed.everyweather.domain.model.weather

data class ForecastDay(
    val date: String,

    val dateEpoch: Long,

    val day: Day,

    val astro: Astro,

    val hourForecast : List<Hour>,

    var isExpanded: Boolean = false
)