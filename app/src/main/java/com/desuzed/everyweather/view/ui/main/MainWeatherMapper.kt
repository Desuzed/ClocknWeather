package com.desuzed.everyweather.view.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.ui.HourUi

class MainWeatherMapper(private val resources: Resources) {

    fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi {
        val weatherMainInfo = WeatherMainInfoUi(response = response, res = resources)
        val hourList = HourUi.generateCurrentDayList(response).map {
            HourUi(
                hour = it,
                timeZone = response.location.timezone,
                res = resources
            )
        }
        val detailCard = DetailCardMain(
            response = response,
            res = resources,
            timeZone = response.location.timezone
        )
        return WeatherMainUi(
            mainInfo = weatherMainInfo,
            hourList = hourList,
            detailCard = detailCard
        )
    }
}