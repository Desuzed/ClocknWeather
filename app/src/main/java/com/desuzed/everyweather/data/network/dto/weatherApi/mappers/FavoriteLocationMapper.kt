package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto

class FavoriteLocationMapper : EntityMapper<FavoriteLocationDto, LocationApp> {
    override fun mapFromEntity(entity: FavoriteLocationDto): LocationApp {
       return LocationApp(
           entity.lat.toFloat(),
           entity.lon.toFloat(),
           entity.cityName,
           entity.region,
           entity.country
        )
    }

}