package com.desuzed.everyweather.view.entity

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.entity.Hour
import com.desuzed.everyweather.model.entity.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherEntityView(private val response: WeatherResponse, res: Resources) {
    @SuppressLint("SimpleDateFormat")
    private val sdfDate = SimpleDateFormat("dd/MM")

    @SuppressLint("SimpleDateFormat")
    private val sdfTime = SimpleDateFormat("HH:mm")
    private val localTime = response.location.localtimeEpoch.times(1000)

    val timeZone = response.location.timezone
    val location = response.location
    val iconUrl: String
    val date: String
    val time: String
    val place: String
    val currentTemp: String
    val description: String
    val feelsLike: String
    val humidity: String
    val pressure: String
    val pop: String
    val wind: String
    val sun: String
    val moon: String

    init {
        sdfDate.timeZone = TimeZone.getTimeZone(timeZone)
        sdfTime.timeZone = TimeZone.getTimeZone(timeZone)
        val current = response.current
        val forecastDay = response.forecastDay[0]
        iconUrl = "https:${current.icon}"
        date = sdfDate.format(localTime)
        time = sdfTime.format(localTime)
        place = response.location.toString()
        currentTemp = current.temp.roundToInt().toString() + res.getString(R.string.celsius)
        description = current.text
        feelsLike = res.getString(R.string.feels_like) + " ${current.feelsLike.roundToInt()}" +
                res.getString(R.string.celsius)
        humidity = "${current.humidity}%"
        pressure = "${current.pressureMb} " + res.getString(R.string.mb)
        pop = "${forecastDay.day.popRain}%, ${current.precipMm} " + res.getString(R.string.mm)       //TODO обработать снежные осадки
        wind = "${current.windSpeed} " + res.getString(R.string.kmh)
        val astro = forecastDay.astro
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }

    /**
     *  Generates list for HourAdapter since current time and plus next 24 items
     */
    @SuppressLint("SimpleDateFormat")
    fun generateCurrentDayList(): List<Hour> {
        val sdf = SimpleDateFormat("H")
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        val hour = sdf.format(localTime).toInt()
        val forecastDay = response.forecastDay
        return forecastDay[0].hourForecast
            .drop(hour)
            .plus(forecastDay[1].hourForecast.take(hour))
    }

    //TODO Bug : На разных языках ставит либо точку либо запятую, хотя данные с апи приходят через точку
    fun buildFavoriteLocationObj(): FavoriteLocationDto {
        return FavoriteLocationDto(
            FavoriteLocationDto.generateKey(location),
            location.name,
            location.region,
            location.country,
            location.lat.toString(),
            location.lon.toString()
        )
    }
}