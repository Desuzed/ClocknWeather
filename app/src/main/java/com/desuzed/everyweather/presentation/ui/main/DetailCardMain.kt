package com.desuzed.everyweather.presentation.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.presentation.ui.base.DetailCard

class DetailCardMain(
    response: WeatherResponse,
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
        val current = response.current
        val forecastDay = response.forecastDay[0]
        val astro = forecastDay.astro
        humidity = "${current.humidity}%"
        pressure = "${current.pressureMb} " + res.getString(R.string.mb)
        pop =
            "${forecastDay.day.popRain}%, ${current.precipMm} " + res.getString(R.string.mm)       //TODO обработать снежные осадки
        wind = "${current.windSpeed} " + res.getString(R.string.kmh)
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}