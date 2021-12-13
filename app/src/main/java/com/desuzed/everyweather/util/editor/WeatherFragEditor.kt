package com.desuzed.everyweather.util.editor

import android.annotation.SuppressLint
import android.content.Context
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.model.Hour
import com.desuzed.everyweather.model.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherFragEditor(private val response: WeatherResponse, context: Context): StringEditor {
    private val res = context.resources
    @SuppressLint("SimpleDateFormat")
    private val sdfDate = SimpleDateFormat("dd/MM")
    @SuppressLint("SimpleDateFormat")
    private val sdfTime = SimpleDateFormat("HH:mm")
    val timeZone = response.location.tzId
    val location = response.location
    private val date = response.location.localtime_epoch.times(1000)
    init {
        sdfDate.timeZone = TimeZone.getTimeZone(timeZone)
        sdfTime.timeZone = TimeZone.getTimeZone(timeZone)
    }

    override fun getResultMap(): Map<String, String> {
        val resultMap = mutableMapOf<String, String>()
        val current = response.current
        val forecastDay = response.forecastDay[0]
        resultMap["imgIcon"] = "https:${current.icon}"
        resultMap["tvDate"] = sdfDate.format(date)
        resultMap["tvTime"] = sdfTime.format(date)
        resultMap["tvPlace"] = response.location.toString()
        resultMap["tvCurrentTemp"] = current.temp.roundToInt().toString() + res.getString(R.string.celsius)
        resultMap["tvDescription"] = current.text
        resultMap["tvFeelsLike"] = res.getString(R.string.feels_like) + " ${current.feelsLike.roundToInt()}" + res.getString(
            R.string.celsius
        )
        resultMap["tvHumidity"] = "${current.humidity}%"
        resultMap["tvPressure"] = "${current.pressureMb} " + res.getString(R.string.mb)
        resultMap["tvPop"] = "${forecastDay.day.popRain}%, ${current.precipMm} " + res.getString(R.string.mm) //TODO обработать снежные осадки
        resultMap["tvWind"] = "${current.windSpeed} " + res.getString(R.string.kmh)
        val astro = forecastDay.astro
        resultMap["tvSun"] = "${astro.sunrise}\n${astro.sunset}"
        resultMap["tvMoon"] = "${astro.moonrise}\n${astro.moonset}"
        return resultMap
    }

    /**
     *  Generates list for HourAdapter since current time and plus next 24 items
     */
    @SuppressLint("SimpleDateFormat")
    fun generateCurrentDayList(): List<Hour> {
        val sdf = SimpleDateFormat("H")
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        val hour = sdf.format(date).toInt()
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