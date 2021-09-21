package com.desuzed.clocknweather.retrofit.pojo

class LatLon(var lat : Double, var lon : Double) {
    override fun toString(): String {
        return "$lat,$lon"
    }
}