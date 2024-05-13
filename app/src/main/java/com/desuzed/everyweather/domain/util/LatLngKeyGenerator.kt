package com.desuzed.everyweather.domain.util

import com.desuzed.everyweather.domain.model.weather.Location

interface LatLngKeyGenerator {
    fun generateKey(location: Location): String
}