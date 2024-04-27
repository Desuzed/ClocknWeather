package com.desuzed.everyweather.data.repository.settings

import androidx.datastore.preferences.core.stringPreferencesKey
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.repository.settings.DatastoreApiProvider
import com.desuzed.everyweather.domain.repository.settings.WeatherSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherSettingRepositoryImpl(
    private val datastoreProvider: DatastoreApiProvider,
) : WeatherSettingsRepository {

    override suspend fun setDistanceDimension(dimension: DistanceDimen) =
        datastoreProvider.edit { preferences ->
            preferences[KEY_DISTANCE_DIMENSION] = dimension.dimensionName
        }

    override val distanceDimen: Flow<DistanceDimen> = datastoreProvider.getFlowOf(
        KEY_DISTANCE_DIMENSION, DistanceDimen.METRIC_KMH.dimensionName
    ).map { distanceDimenStrValue ->
        when (distanceDimenStrValue) {
            DistanceDimen.IMPERIAL.dimensionName -> DistanceDimen.IMPERIAL
            DistanceDimen.METRIC_MS.dimensionName -> DistanceDimen.METRIC_MS
            else -> DistanceDimen.METRIC_KMH
        }
    }

    override suspend fun setTemperatureDimension(dimension: TempDimen) =
        datastoreProvider.edit { preferences ->
            preferences[KEY_TEMPERATURE_DIMENSION] = dimension.dimensionName
        }

    override val tempDimen: Flow<TempDimen> =
        datastoreProvider.getFlowOf(KEY_TEMPERATURE_DIMENSION, TempDimen.CELCIUS.dimensionName)
            .map { tempDimenStrValue ->
                when (tempDimenStrValue) {
                    TempDimen.FAHRENHEIT.dimensionName -> TempDimen.FAHRENHEIT
                    else -> TempDimen.CELCIUS
                }
            }

    override suspend fun setPressureDimension(dimension: PressureDimen) =
        datastoreProvider.edit { preferences ->
            preferences[KEY_PRESSURE_DIMENSION] = dimension.dimensionName
        }

    override val pressureDimen: Flow<PressureDimen> = datastoreProvider.getFlowOf(
        KEY_PRESSURE_DIMENSION, PressureDimen.MILLIMETERS.dimensionName
    ).map { pressureDimenStrValue ->
        when (pressureDimenStrValue) {
            PressureDimen.MILLIBAR.dimensionName -> PressureDimen.MILLIBAR
            PressureDimen.INCHES.dimensionName -> PressureDimen.INCHES
            else -> PressureDimen.MILLIMETERS

        }
    }

    companion object {
        private val KEY_DISTANCE_DIMENSION = stringPreferencesKey("distance_dimension")
        private val KEY_TEMPERATURE_DIMENSION = stringPreferencesKey("temperature_dimension")
        private val KEY_PRESSURE_DIMENSION = stringPreferencesKey("pressure_dimension")
    }

}