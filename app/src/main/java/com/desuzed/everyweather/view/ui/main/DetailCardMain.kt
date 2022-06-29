package com.desuzed.everyweather.view.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.ui.DetailCard
import java.text.SimpleDateFormat
import java.util.*

class DetailCardMain(
    response: WeatherResponse,
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