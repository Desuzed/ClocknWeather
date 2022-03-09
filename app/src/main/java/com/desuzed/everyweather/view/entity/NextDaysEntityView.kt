package com.desuzed.everyweather.view.entity

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.ForecastDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class NextDaysEntityView(
    forecastDay: ForecastDay,
    timeZone: String,
    res: Resources
) {

    @SuppressLint("SimpleDateFormat")
    private val sdfEddMM = SimpleDateFormat("E. dd/MM", Locale.getDefault())
    val iconUrl: String
    val date: String
    val description: String
    val maxTemp: String
    val minTemp: String
    val wind: String
    val pressure: String
    val humidity: String
    val pop: String
    val sun: String
    val moon: String

    init {
        sdfEddMM.timeZone = TimeZone.getTimeZone(timeZone)
        val day = forecastDay.day
        val astro = forecastDay.astro
        iconUrl = "https:${day.icon}"
        date = sdfEddMM.format(forecastDay.dateEpoch * 1000)
        description = day.text
        maxTemp = day.maxTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
        minTemp = day.minTemp.roundToInt().toString() + res.getString(
            R.string.celsius
        )
        wind = "${day.maxWind} " + res.getString(R.string.kmh)
        pressure = "${forecastDay.hourForecast[0].pressureMb} " + res.getString(R.string.mb)
        humidity = "${day.avgHumidity}%"
        pop = "${day.popRain}%, ${day.totalPrecip} " + res.getString(R.string.mm)
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}