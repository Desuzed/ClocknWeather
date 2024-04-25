package com.desuzed.everyweather.domain.repository.settings

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import kotlinx.coroutines.flow.Flow

interface WeatherSettingsRepository {

    suspend fun setDistanceDimension(dimension: DistanceDimen)

    val distanceDimen: Flow<WindSpeed>

    suspend fun setTemperatureDimension(dimension: TempDimen)

    val tempDimen: Flow<Temperature>

    suspend fun setPressureDimension(dimension: PressureDimen)

    val pressureDimen: Flow<Pressure>

    fun getDistanceItemsList(): List<SettingUiItem<DistanceDimen>>

    fun getTemperatureItemsList(): List<SettingUiItem<TempDimen>>

    fun getPressureItemsList(): List<SettingUiItem<PressureDimen>>

}