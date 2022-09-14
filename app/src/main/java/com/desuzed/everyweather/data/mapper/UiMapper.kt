package com.desuzed.everyweather.data.mapper

import android.content.Context
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherResponse
import com.desuzed.everyweather.presentation.ui.main.MainWeatherMapper
import com.desuzed.everyweather.presentation.ui.main.WeatherMainUi
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysMapper
import com.desuzed.everyweather.presentation.ui.next_days.NextDaysUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UiMapper(
    private val windSpeed: WindSpeed,
    private val temperature: Temperature,
    private val language: Language,
    private val pressure: Pressure,
    context: Context
) {
    private val resources = context.resources

    suspend fun mapToNextDaysUi(response: WeatherResponse): List<NextDaysUi> =
        withContext(Dispatchers.IO) {
            NextDaysMapper(
                language = language,
                windSpeed = windSpeed,
                temperature = temperature,
                resources = resources,
                pressure = pressure,
            ).mapToNextDaysList(response)
        }

    suspend fun mapToMainWeatherUi(response: WeatherResponse): WeatherMainUi =
        withContext(Dispatchers.IO) {
            MainWeatherMapper(
                resources = resources,
                windSpeed = windSpeed,
                temperature = temperature,
                pressure = pressure,
            ).mapToMainWeatherUi(response)
        }
}