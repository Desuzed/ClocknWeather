package com.desuzed.everyweather.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.desuzed.everyweather.model.model.Location
import java.math.RoundingMode
import java.text.DecimalFormat

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

    companion object {
        fun generateKey(location: Location) : String{
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            return "${df.format(location.lat)},${df.format(location.lon)}"
        }
    }
}