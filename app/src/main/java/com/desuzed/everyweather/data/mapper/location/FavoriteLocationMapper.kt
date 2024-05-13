package com.desuzed.everyweather.data.mapper.location

import com.desuzed.everyweather.data.mapper.DomainMapper
import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.FavoriteLocation

class FavoriteLocationMapper : EntityMapper<FavoriteLocationDto, FavoriteLocation>,
    DomainMapper<FavoriteLocation, FavoriteLocationDto> {

    override fun mapFromEntity(entity: FavoriteLocationDto): FavoriteLocation =
        FavoriteLocation(
            latLon = entity.latLon,
            cityName = entity.cityName,
            region = entity.region,
            country = entity.country,
            lat = entity.lat,
            lon = entity.lon,
            customName = entity.customName,
        )

    override fun mapFromDomain(domain: FavoriteLocation): FavoriteLocationDto =
        FavoriteLocationDto(
            latLon = domain.latLon,
            cityName = domain.cityName,
            region = domain.region,
            country = domain.country,
            lat = domain.lat,
            lon = domain.lon,
            customName = domain.customName,
        )
}