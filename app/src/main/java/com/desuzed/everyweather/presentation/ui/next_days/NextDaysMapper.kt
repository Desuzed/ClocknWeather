package com.desuzed.everyweather.presentation.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.presentation.ui.HourUi

class NextDaysMapper(
    private val windSpeed: DistanceDimen,
    private val temperature: TempDimen,
    private val language: Lang,
    private val resources: Resources,
    private val pressure: PressureDimen,
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