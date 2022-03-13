package com.desuzed.everyweather.model.entity

import android.location.Location
import com.desuzed.everyweather.util.EntityMapper

class UserLatLng(val lat: Float, val lon: Float, val time: Long) {

    override fun toString(): String {
        return "$lat, $lon"
    }

//    fun toStringInfoFields(): String {
//        return "$cityName, $region, $country"
//    }
//    fun hasLocationInfo () : Boolean = cityName.isNotEmpty() && region.isNotEmpty() && country.isNotEmpty()

}

class UserLatLngMapper : EntityMapper<Location, UserLatLng> {
    override fun mapFromEntity(entity: Location): UserLatLng {
        return UserLatLng(entity.latitude.toFloat(), entity.longitude.toFloat(), entity.time)
    }
}