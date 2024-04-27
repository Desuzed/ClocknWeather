package com.desuzed.everyweather.domain.repository.settings

import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import kotlinx.coroutines.flow.Flow

interface WeatherSettingsRepository {

    suspend fun setDistanceDimension(dimension: DistanceDimen)

    val distanceDimen: Flow<DistanceDimen>

    suspend fun setTemperatureDimension(dimension: TempDimen)

    val tempDimen: Flow<TempDimen>

    suspend fun setPressureDimension(dimension: PressureDimen)

    val pressureDimen: Flow<PressureDimen>

}