package com.desuzed.everyweather.domain.model.weather

class Location(
    val name: String,

    val region: String,

    val country: String,

    val lat: Float,

    val lon: Float,

    val timezone: String,

    val localtimeEpoch: Long,

    val localtime: String
){
    /**
     * toString made for showing correct text
     */
    override fun toString(): String {
        return if (region.isNotEmpty()) {
            "$name, $region"
        } else name
    }
}