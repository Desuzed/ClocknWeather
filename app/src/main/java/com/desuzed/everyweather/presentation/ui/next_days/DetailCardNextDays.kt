package com.desuzed.everyweather.presentation.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.ForecastDay
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.presentation.ui.base.DetailCard

class DetailCardNextDays(
    windSpeed: WindSpeed,
    forecastDay: ForecastDay,
    res: Resources,
) : DetailCard() {

    override val wind: String
    override val pressure: String
    override val humidity: String
    override val pop: String
    override val sun: String
    override val moon: String

    init {
        val day = forecastDay.day
        val astro = forecastDay.astro

        val windSpeedDimen = DistanceDimen.valueOf(windSpeed.id.uppercase())
        val windValue: String
        val precipitation: String
        when (windSpeedDimen) {
            DistanceDimen.METRIC -> {
                windValue = "${day.maxWindKph} "
                precipitation = "${day.totalPrecipMm} " + res.getString(R.string.mm)
            }
            DistanceDimen.IMPERIAL -> {
                windValue = "${day.maxWindMph} "
                precipitation = "${day.totalPrecipInch} " + res.getString(R.string.inch)
            }
        }
        pop = "${forecastDay.day.popRain}%, " + precipitation
        wind = windValue + res.getString(windSpeed.valueStringId)
        pressure = "${forecastDay.hourForecast[0].pressureMb} " + res.getString(R.string.mb)
        humidity = "${day.avgHumidity}%"
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}