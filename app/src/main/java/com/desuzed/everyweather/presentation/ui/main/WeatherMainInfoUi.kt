package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.util.Constants.HTTPS_SCHEME
import com.desuzed.everyweather.util.DateFormatter
import kotlin.math.roundToInt

class WeatherMainInfoUi(
    temperature: TempDimen,
    response: WeatherContent,
    res: Resources,
) {

    private val localTime = response.location.localtimeEpoch

    val timeZone = response.location.timezone
    val location = response.location
    val currentTemp: String
    val geoText: String
    val feelsLike: String
    val date: String
    val time: String
    val description: String
    val iconUrl: String

    init {
        val current = response.current
        iconUrl = "$HTTPS_SCHEME${current.icon}"
        date = DateFormatter.format(
            pattern = DateFormatter.datePattern,
            timeInMills = localTime,
            timeZone = timeZone
        )
        time = DateFormatter.format(
            pattern = DateFormatter.timePattern,
            timeInMills = localTime,
            timeZone = timeZone
        )
        geoText = response.location.toString()
        val currentTemperature: Float
        val feelsLikeTemperature: Float
        when (temperature) {
            TempDimen.CELCIUS -> {
                currentTemperature = current.tempC
                feelsLikeTemperature = current.feelsLikeC
            }

            TempDimen.FAHRENHEIT -> {
                currentTemperature = current.tempF
                feelsLikeTemperature = current.feelsLikeF
            }
        }
        currentTemp =
            currentTemperature.roundToInt().toString() + res.getString(R.string.dot_temperature)
        description = current.text
        feelsLike = res.getString(R.string.feels_like) + " ${feelsLikeTemperature.roundToInt()}" +
                res.getString(R.string.dot_temperature)
    }
}



