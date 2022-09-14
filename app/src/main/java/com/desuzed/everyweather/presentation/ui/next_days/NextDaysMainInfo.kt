package com.desuzed.everyweather.presentation.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.weather.ForecastDay
import com.desuzed.everyweather.util.DateFormatter
import kotlin.math.roundToInt

class NextDaysMainInfo(
    language: Language,
    temperature: Temperature,
    forecastDay: ForecastDay,
    timeZone: String,
    res: Resources,
) {
    val iconUrl: String
    val date: String
    val description: String
    val maxTemp: String
    val minTemp: String

    init {
        val day = forecastDay.day
        iconUrl = "https:${day.icon}"
        date = DateFormatter.format(
            pattern = DateFormatter.fullDatePattern,
            timeInMills = forecastDay.dateEpoch,
            timeZone = timeZone,
            lang = language.id.lowercase(),
        )
        description = day.text

        val maxTemperature: Float
        val minTemperature: Float
        val id = temperature.id.uppercase()
        val tempDimen = TempDimen.valueOf(id)
        when (tempDimen) {
            TempDimen.CELCIUS -> {
                maxTemperature = day.maxTempC
                minTemperature = day.minTempC
            }
            TempDimen.FAHRENHEIT -> {
                maxTemperature = day.maxTempF
                minTemperature = day.minTempF
            }
        }
        maxTemp = maxTemperature.roundToInt().toString() + res.getString(
            R.string.dot_temperature
        )
        minTemp = minTemperature.roundToInt().toString() + res.getString(
            R.string.dot_temperature
        )
    }
}