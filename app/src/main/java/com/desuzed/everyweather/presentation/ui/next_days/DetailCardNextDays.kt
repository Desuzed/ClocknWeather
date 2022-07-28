package com.desuzed.everyweather.presentation.ui.next_days

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.ForecastDay
import com.desuzed.everyweather.presentation.ui.base.DetailCard

class DetailCardNextDays(
    forecastDay: ForecastDay,
    res: Resources,
) : DetailCard() {

    @SuppressLint("SimpleDateFormat")
    override val wind: String
    override val pressure: String
    override val humidity: String
    override val pop: String
    override val sun: String
    override val moon: String

    init {
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