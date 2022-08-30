package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.presentation.ui.base.DetailCard

class DetailCardMain(
    windSpeed: WindSpeed,
    response: WeatherResponse,
    res: Resources,
) : DetailCard() {

    override val wind: String
    override val pressure: String
    override val humidity: String
    override val pop: String
    override val sun: String
    override val moon: String

    //TODO обработать снежные осадки
    init {
        val current = response.current
        val forecastDay = response.forecastDay[0]
        val astro = forecastDay.astro
        humidity = "${current.humidity}%"
        pressure = "${current.pressureMb} " + res.getString(R.string.mb)
        val windSpeedDimen = DistanceDimen.valueOf(windSpeed.id.uppercase())
        val windValue: String
        val precipitation: String
        when (windSpeedDimen) {
            DistanceDimen.METRIC -> {
                windValue = "${current.windSpeedKph} "
                precipitation = "${current.precipMm} " + res.getString(R.string.mm)
            }
            DistanceDimen.IMPERIAL -> {
                windValue = "${current.windSpeedMph} "
                precipitation = "${current.precipInch} " + res.getString(R.string.inch)
            }
        }
        pop = "${forecastDay.day.popRain}%, " + precipitation
        wind = windValue + res.getString(windSpeed.valueStringId)
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}