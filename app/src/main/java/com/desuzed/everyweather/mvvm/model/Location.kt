package com.desuzed.everyweather.mvvm.model

class Location(
    val name: String,

    val region: String,

    val country: String,

    val lat: Float,

    val lon: Float,

    val tzId: String,

    val localtime_epoch: Long,

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