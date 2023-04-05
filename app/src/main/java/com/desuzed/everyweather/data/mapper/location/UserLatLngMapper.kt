package com.desuzed.everyweather.data.mapper.location

import android.location.Location
import com.desuzed.everyweather.data.mapper.EntityMapper
import com.desuzed.everyweather.domain.model.location.UserLatLng

class UserLatLngMapper : EntityMapper<Location, UserLatLng> {
    override fun mapFromEntity(entity: Location): UserLatLng {
        return UserLatLng(entity.latitude, entity.longitude, entity.time)
    }
}