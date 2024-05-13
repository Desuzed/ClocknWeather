package com.desuzed.everyweather.presentation.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.weather.ForecastDay
import com.desuzed.everyweather.presentation.ui.base.DetailCard
import com.desuzed.everyweather.presentation.ui.settings.SettingsMapper
import com.desuzed.everyweather.util.DecimalFormatter
import com.desuzed.everyweather.util.DecimalFormatter.MBAR_TO_MMHG_MULTIPLIER

class DetailCardNextDays(
    windSpeed: DistanceDimen,
    forecastDay: ForecastDay,
    pressureDimen: PressureDimen,
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

        val windValue: String
        val precipitation: String
        when (windSpeed) {
            DistanceDimen.METRIC_KMH -> {
                windValue = "${day.maxWindKph} "
                precipitation = "${day.totalPrecipMm} " + res.getString(R.string.mm)
            }

            DistanceDimen.METRIC_MS -> {
                val decimalWindHour = day.maxWindKph.times(DecimalFormatter.KPH_TO_MS_MULTIPLIER)
                val formattedWindSpeed = DecimalFormatter.formatFloat(decimalWindHour)
                windValue = "$formattedWindSpeed "
                precipitation = "${day.totalPrecipMm} " + res.getString(R.string.mm)
            }

            DistanceDimen.IMPERIAL -> {
                windValue = "${day.maxWindMph} "
                precipitation = "${day.totalPrecipInch} " + res.getString(R.string.inch)
            }
        }
        val pressureValue = when (pressureDimen) {
            PressureDimen.MILLIBAR -> forecastDay.hourForecast[0].pressureMb
            PressureDimen.MILLIMETERS -> {
                forecastDay.hourForecast[0].pressureMb.times(MBAR_TO_MMHG_MULTIPLIER)
            }

            PressureDimen.INCHES -> forecastDay.hourForecast[0].pressureInch
        }
        val formattedPressure = DecimalFormatter.formatFloat(pressureValue)
        val percent = res.getString(R.string.percent)
        val windSpeedUi = SettingsMapper.getSelectedWindSpeed(windSpeed)
        val pressureUi = SettingsMapper.getSelectedPressure(pressureDimen)
        pop = "${forecastDay.day.popRain}$percent, " + precipitation
        wind = windValue + res.getString(windSpeedUi.valueStringId)
        pressure = "$formattedPressure " + res.getString(pressureUi.valueStringId)
        humidity = "${day.avgHumidity}$percent"
        sun = "${astro.sunrise}\n${astro.sunset}"
        moon = "${astro.moonrise}\n${astro.moonset}"
    }
}