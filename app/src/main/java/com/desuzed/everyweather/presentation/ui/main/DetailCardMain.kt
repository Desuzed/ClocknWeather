package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.presentation.ui.base.DetailCard
import com.desuzed.everyweather.presentation.ui.settings.SettingsMapper
import com.desuzed.everyweather.util.DecimalFormatter

class DetailCardMain(
    windSpeed: DistanceDimen,
    pressureDimen: PressureDimen,
    response: WeatherContent,
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
        val windValue: String
        val precipitation: String
        when (windSpeed) {
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
        val pressureValue = when (pressureDimen) {
            PressureDimen.MILLIBAR -> current.pressureMb
            PressureDimen.MILLIMETERS -> {
                current.pressureMb.times(DecimalFormatter.MBAR_TO_MMHG_MULTIPLIER)
            }

            PressureDimen.INCHES -> current.pressureInch
        }
        val windSpeedUi = SettingsMapper.getSelectedWindSpeed(windSpeed)
        val pressureUi = SettingsMapper.getSelectedPressure(pressureDimen)
        val formattedPressure = DecimalFormatter.formatFloat(pressureValue)
        pressure = "$formattedPressure " + res.getString(pressureUi.valueStringId)
        pop = "${forecastDay.day.popRain}%, " + precipitation
        wind = windValue + res.getString(windSpeedUi.valueStringId)
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}