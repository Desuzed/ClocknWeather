package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto

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