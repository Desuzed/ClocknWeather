package com.desuzed.everyweather.view.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.Hour
import com.desuzed.everyweather.model.entity.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourUi(
    hour: Hour,
    timeZone: String,
    res: Resources
) {
    @SuppressLint("SimpleDateFormat")
    private val sdfHHmm = SimpleDateFormat("HH:mm")
    val time: String
    val temp: String
    val wind: String
    val iconUrl: String
    val rotation: Float

    init {
        sdfHHmm.timeZone = TimeZone.getTimeZone(timeZone)
        time = sdfHHmm.format(hour.timeEpoch * 1000)
        temp = hour.temp.roundToInt().toString() + res.getString(R.string.celsius)
        wind = "${hour.windSpeed.toInt()} " + res.getString(R.string.kmh)
        iconUrl = "https:${hour.icon}"
        rotation = hour.windDegree.toFloat() - 180
    }


    companion object {

        /**
         *  Generates list for HourAdapter since current time and plus next 24 items
         */
        @SuppressLint("SimpleDateFormat")
        fun generateCurrentDayList(response: WeatherResponse): List<Hour> {
            val sdf = SimpleDateFormat("H")
            sdf.timeZone = TimeZone.getTimeZone(response.location.timezone)
            val hour = sdf.format(response.location.localtimeEpoch.times(1000)).toInt()
            val forecastDay = response.forecastDay
            return forecastDay[0].hourForecast
                .drop(hour)
                .plus(forecastDay[1].hourForecast.take(hour))
        }
    }

}