package com.desuzed.everyweather.model.entity

import android.location.Location
import com.desuzed.everyweather.util.EntityMapper

class UserLatLng(val lat: Float, val lon: Float, val time: Long) {

    override fun toString(): String {
        return "$lat, $lon"
    }

}

class UserLatLngMapper : EntityMapper<Location, UserLatLng> {
    override fun mapFromEntity(entity: Location): UserLatLng {
        return UserLatLng(entity.latitude.toFloat(), entity.longitude.toFloat(), entity.time)
    }
}