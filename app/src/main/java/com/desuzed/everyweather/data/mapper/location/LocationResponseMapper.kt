package com.desuzed.everyweather.data.mapper.location

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.location_iq.LocationResult
import com.desuzed.everyweather.domain.model.location.geo.GeoData

class LocationResponseMapper : EntityMapper<List<LocationResult>, List<GeoData>> {
    override fun mapFromEntity(entity: List<LocationResult>): List<GeoData> =
        entity.map {
            GeoData(
                lat = it.lat,
                lon = it.lon,
                name = it.name,
                importance = it.importance
            )
        }
}