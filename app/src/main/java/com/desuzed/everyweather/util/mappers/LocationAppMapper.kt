package com.desuzed.everyweather.util.mappers

import android.location.Location
import com.desuzed.everyweather.mvvm.LocationApp

class LocationAppMapper: EntityMapper<Location, LocationApp> {
    override fun mapFromEntity(entity: Location): LocationApp {
        return LocationApp (entity.latitude.toFloat(), entity.longitude.toFloat())
    }
}