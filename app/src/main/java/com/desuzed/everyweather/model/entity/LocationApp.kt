package com.desuzed.everyweather.model.entity

import android.location.Location
import com.desuzed.everyweather.util.EntityMapper

class LocationApp(val lat: Float, val lon: Float, val time: Long) {
    constructor(
        lat: Float,
        lon: Float,
        time: Long,
        _cityName: String,
        _region: String,
        _country: String
    ) : this(lat, lon, time) {
        this.cityName = _cityName
        this.region = _region
        this.country = _country
    }

    private var cityName: String = ""
    private var region: String = ""
    private var country: String = ""

    //private var time =
    override fun toString(): String {
        return "$lat, $lon"
    }

//    fun toStringInfoFields(): String {
//        return "$cityName, $region, $country"
//    }
//    fun hasLocationInfo () : Boolean = cityName.isNotEmpty() && region.isNotEmpty() && country.isNotEmpty()

}

class LocationAppMapper : EntityMapper<Location, LocationApp> {
    override fun mapFromEntity(entity: Location): LocationApp {
        return LocationApp(entity.latitude.toFloat(), entity.longitude.toFloat(), entity.time)
    }
}