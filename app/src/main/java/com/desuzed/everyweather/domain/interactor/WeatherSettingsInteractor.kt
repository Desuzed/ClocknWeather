package com.desuzed.everyweather.domain.interactor

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.repository.settings.WeatherSettingsRepository
import kotlinx.coroutines.flow.Flow

class WeatherSettingsInteractor(private val weatherSettingsRepository: WeatherSettingsRepository) {

    suspend fun setDistanceDimension(dimension: DistanceDimen) =
        weatherSettingsRepository.setDistanceDimension(dimension)

    val distanceDimen: Flow<WindSpeed> = weatherSettingsRepository.distanceDimen

    suspend fun setTemperatureDimension(dimension: TempDimen) =
        weatherSettingsRepository.setTemperatureDimension(dimension)

    val tempDimen: Flow<Temperature> = weatherSettingsRepository.tempDimen

    suspend fun setPressureDimension(dimension: PressureDimen) =
        weatherSettingsRepository.setPressureDimension(dimension)

    val pressureDimen: Flow<Pressure> = weatherSettingsRepository.pressureDimen

    fun getDistanceItemsList(): List<SettingUiItem<DistanceDimen>> =
        weatherSettingsRepository.getDistanceItemsList()

    fun getTemperatureItemsList(): List<SettingUiItem<TempDimen>> =
        weatherSettingsRepository.getTemperatureItemsList()

    fun getPressureItemsList(): List<SettingUiItem<PressureDimen>> =
        weatherSettingsRepository.getPressureItemsList()
}