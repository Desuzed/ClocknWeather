package com.desuzed.everyweather.view.ui.next_days

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.ForecastDay
import com.desuzed.everyweather.view.ui.DetailCard
import java.text.SimpleDateFormat
import java.util.*

class DetailCardNextDays(
    forecastDay: ForecastDay,
    timeZone: String,
    res: Resources,
) : DetailCard() {

    @SuppressLint("SimpleDateFormat")
    private val sdfEddMM = SimpleDateFormat("E. dd/MM", Locale.getDefault())
    override val wind: String
    override val pressure: String
    override val humidity: String
    override val pop: String
    override val sun: String
    override val moon: String

    init {
        sdfEddMM.timeZone = TimeZone.getTimeZone(timeZone)
        val day = forecastDay.day
        val astro = forecastDay.astro
        wind = "${day.maxWind} " + res.getString(R.string.kmh)
        pressure = "${forecastDay.hourForecast[0].pressureMb} " + res.getString(R.string.mb)
        humidity = "${day.avgHumidity}%"
        pop = "${day.popRain}%, ${day.totalPrecip} " + res.getString(R.string.mm)
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}