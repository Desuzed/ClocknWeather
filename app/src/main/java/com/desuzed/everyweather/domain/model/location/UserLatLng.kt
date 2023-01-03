package com.desuzed.everyweather.domain.model.location

data class UserLatLng(val lat: Float, val lon: Float, val time: Long) {

    override fun toString(): String {
        return "$lat, $lon"
    }

}