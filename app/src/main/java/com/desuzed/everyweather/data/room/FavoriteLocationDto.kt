package com.desuzed.everyweather.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.desuzed.everyweather.domain.model.Location
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.util.EntityMapper
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Entity(tableName = "favorite_location_table")
data class FavoriteLocationDto(
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

    fun toQuery(): String = "$lat, $lon"

    companion object {
        fun generateKey(location: Location): String {
            val df = DecimalFormat("#.#", DecimalFormatSymbols(Locale.ENGLISH))
            df.roundingMode = RoundingMode.CEILING
            return "${df.format(location.lat)},${df.format(location.lon)}"
        }

        //TODO Bug : На разных языках ставит либо точку либо запятую, хотя данные с апи приходят через точку
        fun buildFavoriteLocationObj(location: Location): FavoriteLocationDto {
            return FavoriteLocationDto(
                generateKey(location),
                location.name,
                location.region,
                location.country,
                location.lat.toString(),
                location.lon.toString()
            )
        }
    }
}