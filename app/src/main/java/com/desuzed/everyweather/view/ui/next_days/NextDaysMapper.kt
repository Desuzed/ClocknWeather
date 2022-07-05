package com.desuzed.everyweather.view.ui.next_days

import android.content.res.Resources
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.ui.DetailCard
import com.desuzed.everyweather.view.ui.HourUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NextDaysMapper(private val resources: Resources) {

    fun mapToNextDaysList(entity: WeatherResponse): List<NextDaysUi> {
        val timezone = entity.location.timezone
        val resultList = entity.forecastDay.map {
            val hourUiList = it.hourForecast.map { hour ->
                HourUi(hour, timezone, resources)
            }
            val nextDaysMainInfo = NextDaysMainInfo(
                forecastDay = it,
                timeZone = timezone,
                res = resources,
            )
            NextDaysUi(
                nextDaysMainInfo = nextDaysMainInfo,
                detailCard = DetailCardNextDays(
                    forecastDay = it,
                    res = resources,
                ),
                hourList = hourUiList,
            )
        }
        return resultList
    }

}