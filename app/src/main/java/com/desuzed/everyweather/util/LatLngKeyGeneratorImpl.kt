package com.desuzed.everyweather.util

import com.desuzed.everyweather.domain.model.weather.Location
import com.desuzed.everyweather.domain.util.LatLngKeyGenerator

class LatLngKeyGeneratorImpl : LatLngKeyGenerator {

    override fun generateKey(location: Location): String {
        val lat = DecimalFormatter.formatFloatWithRoundingMode(location.lat)
        val lon = DecimalFormatter.formatFloatWithRoundingMode(location.lon)
        return "$lat,$lon"
    }
}