package com.desuzed.everyweather

object Config {
    val debug = BuildConfig.DEBUG
    const val baseWeatherApiUrl = "https://api.weatherapi.com/v1/"
    const val locationIqBaseUrl = "https://api.locationiq.com/v1/"
    const val weatherApiName = "weatherApi"
    const val locationIqApi = "locationIqApi"
    const val TIMEOUT = 30L
}