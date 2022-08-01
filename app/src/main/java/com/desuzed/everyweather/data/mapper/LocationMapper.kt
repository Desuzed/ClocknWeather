package com.desuzed.everyweather.data.mapper

import com.desuzed.everyweather.data.network.dto.weatherApi.LocationDto
import com.desuzed.everyweather.domain.model.Location
import com.desuzed.everyweather.util.EntityMapper

class LocationMapper : EntityMapper<LocationDto, Location> {
    override fun mapFromEntity(entity: LocationDto): Location {
        return Location(
            entity.name,
            entity.region,
            entity.country,
            entity.lat,
            entity.lon,
            entity.timeZone,
            entity.localtimeEpoch,
            entity.localtime
        )
    }
}