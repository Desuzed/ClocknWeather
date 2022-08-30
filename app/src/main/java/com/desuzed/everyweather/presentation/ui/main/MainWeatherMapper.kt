package com.desuzed.everyweather.presentation.ui.main

import android.content.res.Resources
import com.desuzed.everyweather.domain.model.WeatherResponse
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.presentation.ui.HourUi

class MainWeatherMapper(
    private val windSpeed: WindSpeed,
    private val temperature: Temperature,
    private val resources: Resources
) {

    fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi {
        val weatherMainInfo = WeatherMainInfoUi(
            temperature = temperature,
            response = response,
            res = resources
        )
        val hourList = HourUi.generateCurrentDayList(response).map {
            HourUi(
                windSpeed = windSpeed,
                temperature = temperature,
                hour = it,
                timeZone = response.location.timezone,
                res = resources
            )
        }
        val detailCard = DetailCardMain(
            response = response,
            res = resources,
            windSpeed = windSpeed,
        )
        return WeatherMainUi(
            mainInfo = weatherMainInfo,
            hourList = hourList,
            detailCard = detailCard
        )
    }
}