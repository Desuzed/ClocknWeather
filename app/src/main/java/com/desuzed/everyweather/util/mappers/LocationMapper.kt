package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.Location
import com.desuzed.everyweather.network.dto.LocationDto

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