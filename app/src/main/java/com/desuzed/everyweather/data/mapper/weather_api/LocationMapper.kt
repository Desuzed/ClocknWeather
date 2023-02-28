package com.desuzed.everyweather.data.mapper.weather_api

import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.LocationDto
import com.desuzed.everyweather.domain.model.weather.Location

class LocationMapper : EntityMapper<LocationDto, Location> {
    override fun mapFromEntity(entity: LocationDto): Location {
        return Location(
            name = entity.name,
            region = entity.region,
            country = entity.country,
            lat = entity.lat,
            lon = entity.lon,
            timezone = entity.timeZone,
            localtimeEpoch = entity.localtimeEpoch,
        )
    }
}