package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherResponse
import com.desuzed.everyweather.presentation.ui.base.DetailCard
import com.desuzed.everyweather.util.DecimalFormatter

class DetailCardMain(
    windSpeed: WindSpeed,
    pressureDimen: Pressure,
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
        val windSpeedDimen = DistanceDimen.valueOf(windSpeed.id.uppercase())
        val windValue: String
        val precipitation: String
        when (windSpeedDimen) {
            DistanceDimen.METRIC_KMH -> {
                windValue = "${current.windSpeedKph} "
                precipitation = "${current.precipMm} " + res.getString(R.string.mm)
            }
            DistanceDimen.METRIC_MS -> {
                val decimalWindHour =
                    current.windSpeedKph.times(DecimalFormatter.KPH_TO_MS_MULTIPLIER)
                val formattedWindSpeed = DecimalFormatter.formatFloat(decimalWindHour)
                windValue = "$formattedWindSpeed "
                precipitation = "${current.precipMm} " + res.getString(R.string.mm)
            }
            DistanceDimen.IMPERIAL -> {
                windValue = "${current.windSpeedMph} "
                precipitation = "${current.precipInch} " + res.getString(R.string.inch)
            }
        }
        val pressureDimension = PressureDimen.valueOf(pressureDimen.id.uppercase())
        val pressureValue = when (pressureDimension) {
            PressureDimen.MILLIBAR -> current.pressureMb
            PressureDimen.MILLIMETERS -> {
                current.pressureMb.times(DecimalFormatter.MBAR_TO_MMHG_MULTIPLIER)
            }
            PressureDimen.INCHES -> current.pressureInch
        }
        val formattedPressure = DecimalFormatter.formatFloat(pressureValue)
        pressure = "$formattedPressure " + res.getString(pressureDimen.valueStringId)
        pop = "${forecastDay.day.popRain}%, " + precipitation
        wind = windValue + res.getString(windSpeed.valueStringId)
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}