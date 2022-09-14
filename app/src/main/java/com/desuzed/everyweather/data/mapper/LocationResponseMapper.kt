package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.location_iq.LocationResult
import com.desuzed.everyweather.domain.model.location.geo.GeoResponse
import com.desuzed.everyweather.util.EntityMapper

class LocationResponseMapper : EntityMapper<List<LocationResult>, List<GeoResponse>> {
    override fun mapFromEntity(entity: List<LocationResult>): List<GeoResponse> =
        entity.map {
            GeoResponse(
                lat = it.lat,
                lon = it.lon,
                name = it.name,
                importance = it.importance
            )
        }
}