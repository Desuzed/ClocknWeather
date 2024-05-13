package com.desuzed.everyweather.domain.interactor

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.repository.settings.WeatherSettingsRepository
import kotlinx.coroutines.flow.Flow

class WeatherSettingsInteractor(private val weatherSettingsRepository: WeatherSettingsRepository) {

    suspend fun setDistanceDimension(dimension: DistanceDimen) =
        weatherSettingsRepository.setDistanceDimension(dimension)

    val distanceDimen: Flow<DistanceDimen> = weatherSettingsRepository.distanceDimen

    suspend fun setTemperatureDimension(dimension: TempDimen) =
        weatherSettingsRepository.setTemperatureDimension(dimension)

    val tempDimen: Flow<TempDimen> = weatherSettingsRepository.tempDimen

    suspend fun setPressureDimension(dimension: PressureDimen) =
        weatherSettingsRepository.setPressureDimension(dimension)

    val pressureDimen: Flow<PressureDimen> = weatherSettingsRepository.pressureDimen

}