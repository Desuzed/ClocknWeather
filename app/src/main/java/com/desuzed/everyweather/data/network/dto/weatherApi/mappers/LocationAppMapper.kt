package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import android.location.Location
import com.desuzed.everyweather.model.model.LocationApp

class LocationAppMapper: EntityMapper<Location, LocationApp> {
    override fun mapFromEntity(entity: Location): LocationApp {
        return LocationApp (entity.latitude.toFloat(), entity.longitude.toFloat())
    }
}