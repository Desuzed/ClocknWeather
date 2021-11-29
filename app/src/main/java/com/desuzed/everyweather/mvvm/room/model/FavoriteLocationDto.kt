package com.desuzed.everyweather.mvvm.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_location_table")
class FavoriteLocationDto(
    /**
     * latLon is made to be unique key by concatenating latitude and longitude values
     */
    @PrimaryKey @ColumnInfo(name = "lat_lon") val latLon: String,
    @ColumnInfo(name = "name") val cityName: String,
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lon") val lon: String,
) {
    override fun toString(): String {
        return if (region.isNotEmpty()) {
            "$region, $country"
        } else country
    }
}