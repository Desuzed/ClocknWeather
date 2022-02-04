package com.desuzed.everyweather.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lat_lng_table")
class LatLngDto(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lon") val lon: String,
)

//class FavoriteLocationMapper : EntityMapper<FavoriteLocationDto, LocationApp> {
//    override fun mapFromEntity(entity: FavoriteLocationDto): LocationApp {
//        return LocationApp(
//            entity.lat.toFloat(),
//            entity.lon.toFloat(),
//            entity.cityName,
//            entity.region,
//            entity.country
//        )
//    }
//
//}