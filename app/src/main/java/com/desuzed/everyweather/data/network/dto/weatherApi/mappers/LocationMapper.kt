package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import com.desuzed.everyweather.model.model.Location
import com.desuzed.everyweather.data.network.dto.weatherApi.LocationDto

class LocationMapper : EntityMapper<LocationDto, Location> {
    override fun mapFromEntity(entity: LocationDto): Location {
        return Location(
            entity.name,
            entity.region,
            entity.country,
            entity.lat,
            entity.lon,
            entity.tzId,
            entity.localtime_epoch,
            entity.localtime
        )
    }
}