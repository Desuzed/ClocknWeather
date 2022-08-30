package com.desuzed.everyweather.presentation.ui

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.Hour
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.util.DateFormatter
import kotlin.math.roundToInt

class HourUi(
    windSpeed: WindSpeed,
    temperature: Temperature,
    hour: Hour,
    timeZone: String,
    res: Resources
) {

    val time: String
    val temp: String
    val wind: String
    val iconUrl: String
    val rotation: Float

    init {
        time = DateFormatter.format(
            pattern = DateFormatter.timePattern,
            timeInMills = hour.timeEpoch,
            timeZone = timeZone,
        )

        //todo ТУТ
        val tempDimen = TempDimen.valueOf(temperature.id.uppercase())
        val tempHour = when (tempDimen) {
            TempDimen.CELCIUS -> hour.tempC.roundToInt()
            TempDimen.FAHRENHEIT -> hour.tempF.roundToInt()
        }
        temp = tempHour.toString() + res.getString(R.string.dot_temperature)
        val windSpeedDimen = DistanceDimen.valueOf(windSpeed.id.uppercase())
        val windHour = when (windSpeedDimen) {
            DistanceDimen.METRIC -> hour.windSpeedKmh.toInt()
            DistanceDimen.IMPERIAL -> hour.windSpeedMph.toInt()
        }
        wind = "$windHour " + res.getString(windSpeed.valueStringId)
        iconUrl = "https:${hour.icon}"
        rotation = hour.windDegree.toFloat() - 180
    }

    companion object {

        /**
         *  Generates list for HourAdapter since current time and plus next 24 items
         */
        fun generateCurrentDayList(response: WeatherResponse): List<Hour> {
            val hour = DateFormatter.format(
                pattern = DateFormatter.hourPattern,
                timeInMills = response.location.localtimeEpoch,
                timeZone = response.location.timezone,
            ).toInt()
            val forecastDay = response.forecastDay
            return forecastDay[0].hourForecast
                .drop(hour)
                .plus(forecastDay[1].hourForecast.take(hour))
        }
    }

}