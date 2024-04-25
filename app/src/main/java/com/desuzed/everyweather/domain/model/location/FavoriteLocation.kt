package com.desuzed.everyweather.domain.model.location

data class FavoriteLocation(
    val latLon: String,
    val cityName: String,
    val region: String,
    val country: String,
    val lat: String,
    val lon: String,
    val customName: String,
) {
    fun toQuery(): String = "$lat, $lon"

    fun fullName(): String {
        return if (region.isNotEmpty()) {
            "$region, $country"
        } else country
    }
}
