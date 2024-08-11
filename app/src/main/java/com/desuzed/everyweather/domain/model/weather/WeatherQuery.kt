package com.desuzed.everyweather.domain.model.weather

import com.desuzed.everyweather.domain.model.location.UserLatLng
import kotlinx.serialization.Serializable

@Serializable
data class WeatherQuery(
    val id: String,
    val query: String,
    val userChosenMapPoint: UserLatLng? = null,
    val shouldTriggerWeatherRequest: Boolean = false,
)
