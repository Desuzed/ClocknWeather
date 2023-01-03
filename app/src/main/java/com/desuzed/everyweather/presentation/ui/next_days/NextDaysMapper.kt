package com.desuzed.everyweather.presentation.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.presentation.ui.HourUi

class NextDaysMapper(
    private val windSpeed: WindSpeed,
    private val temperature: Temperature,
    private val language: Language,
    private val resources: Resources,
    private val pressure: Pressure,
) {

    fun mapToNextDaysList(entity: WeatherContent): List<NextDaysUi> {
        val timezone = entity.location.timezone
        val resultList = entity.forecastDay.map {
            val hourUiList = it.hourForecast.map { hour ->
                HourUi(
                    windSpeed = windSpeed,
                    temperature = temperature,
                    hour = hour,
                    timeZone = timezone,
                    res = resources,
                )
            }
            val nextDaysMainInfo = NextDaysMainInfo(
                language = language,
                temperature = temperature,
                forecastDay = it,
                timeZone = timezone,
                res = resources,
            )
            NextDaysUi(
                nextDaysMainInfo = nextDaysMainInfo,
                detailCard = DetailCardNextDays(
                    windSpeed = windSpeed,
                    pressureDimen = pressure,
                    forecastDay = it,
                    res = resources,
                ),
                hourList = hourUiList,
            )
        }
        return resultList
    }

}