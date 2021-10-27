package com.desuzed.clocknweather.network.dto

class LatLon(var lat : Double, var lon : Double) {
    override fun toString(): String {
        return "$lat,$lon"
    }
}