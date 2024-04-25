package com.desuzed.everyweather.data.repository.settings

import androidx.datastore.preferences.core.stringPreferencesKey
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
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

    override val distanceDimen: Flow<WindSpeed> = datastoreProvider.getFlowOf(
        KEY_DISTANCE_DIMENSION, DistanceDimen.METRIC_KMH.dimensionName
    ).map { distanceDimenId ->
        val distValueId = when (distanceDimenId) {
            DistanceDimen.IMPERIAL.dimensionName -> R.string.mph
            DistanceDimen.METRIC_MS.dimensionName -> R.string.ms
            else -> R.string.kmh
        }
        WindSpeed(
            id = distanceDimenId,
            categoryStringId = R.string.distance_dimension,
            valueStringId = distValueId,
        )
    }

    override suspend fun setTemperatureDimension(dimension: TempDimen) =
        datastoreProvider.edit { preferences ->
            preferences[KEY_TEMPERATURE_DIMENSION] = dimension.dimensionName
        }

    override val tempDimen: Flow<Temperature> =
        datastoreProvider.getFlowOf(KEY_TEMPERATURE_DIMENSION, TempDimen.CELCIUS.dimensionName)
            .map { tempDimenId ->
                val tempValueId = when (tempDimenId) {
                    TempDimen.FAHRENHEIT.dimensionName -> R.string.fahrenheit
                    else -> R.string.celcius
                }
                Temperature(
                    id = tempDimenId,
                    categoryStringId = R.string.temperature_dimension,
                    valueStringId = tempValueId,
                )
            }

    override suspend fun setPressureDimension(dimension: PressureDimen) =
        datastoreProvider.edit { preferences ->
            preferences[KEY_PRESSURE_DIMENSION] = dimension.dimensionName
        }

    override val pressureDimen: Flow<Pressure> = datastoreProvider.getFlowOf(
        KEY_PRESSURE_DIMENSION, PressureDimen.MILLIMETERS.dimensionName
    ).map { pressureDimenId ->
        val pressureValueId = when (pressureDimenId) {
            PressureDimen.MILLIBAR.dimensionName -> R.string.mb
            PressureDimen.INCHES.dimensionName -> R.string.inch
            else -> R.string.mmhg

        }
        Pressure(
            id = pressureDimenId,
            categoryStringId = R.string.pressure,
            valueStringId = pressureValueId,
        )
    }

    override fun getDistanceItemsList(): List<SettingUiItem<DistanceDimen>> {
        return DistanceDimen.values().toList().map {
            when (it) {
                DistanceDimen.METRIC_KMH -> SettingUiItem(it, R.string.kmh)
                DistanceDimen.METRIC_MS -> SettingUiItem(it, R.string.ms)
                DistanceDimen.IMPERIAL -> SettingUiItem(it, R.string.mph)
            }
        }
    }

    override fun getTemperatureItemsList(): List<SettingUiItem<TempDimen>> {
        return TempDimen.values().toList().map {
            when (it) {
                TempDimen.FAHRENHEIT -> SettingUiItem(it, R.string.fahrenheit)
                TempDimen.CELCIUS -> SettingUiItem(it, R.string.celcius)
            }
        }
    }

    override fun getPressureItemsList(): List<SettingUiItem<PressureDimen>> {
        return PressureDimen.values().toList().map {
            when (it) {
                PressureDimen.MILLIBAR -> SettingUiItem(it, R.string.mb)
                PressureDimen.MILLIMETERS -> SettingUiItem(it, R.string.mmhg)
                PressureDimen.INCHES -> SettingUiItem(it, R.string.inch)
            }
        }
    }

    companion object {
        private val KEY_DISTANCE_DIMENSION = stringPreferencesKey("distance_dimension")
        private val KEY_TEMPERATURE_DIMENSION = stringPreferencesKey("temperature_dimension")
        private val KEY_PRESSURE_DIMENSION = stringPreferencesKey("pressure_dimension")
    }

}