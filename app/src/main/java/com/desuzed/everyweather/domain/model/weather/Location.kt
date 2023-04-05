package com.desuzed.everyweather.domain.model.weather

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val localtimeEpoch: Long,
) {
    /**
     * toString made for showing correct text
     */
    override fun toString(): String {
        return if (region.isNotEmpty()) {
            "$name, $region"
        } else name
    }
}