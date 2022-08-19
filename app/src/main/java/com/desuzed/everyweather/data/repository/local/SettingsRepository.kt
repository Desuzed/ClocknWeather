package com.desuzed.everyweather.data.repository.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    suspend fun setLanguage(lang: Language) = edit { preferences ->
        preferences[KEY_LANGUAGE] = lang.name
    }

    val language: Flow<Language> = getFlowOf(KEY_LANGUAGE, Language.RU.name).map {
        when (it) {//todo mapping
            "ru" -> Language.RU
            else -> Language.EN
        }
    }

    suspend fun setDistanceDimension(dimension: DistanceDimension) = edit { preferences ->
        preferences[KEY_DISTANCE_DIMENSION] = dimension.name
    }

    val distanceDimension: Flow<DistanceDimension> =
        getFlowOf(KEY_DISTANCE_DIMENSION, DistanceDimension.METRIC.name).map {
            when (it) {//todo mapping
                "IMPERIAL" -> DistanceDimension.IMPERIAL
                else -> DistanceDimension.METRIC
            }
        }

    suspend fun setTemperatureDimension(dimension: TemperatureDimension) = edit { preferences ->
        preferences[KEY_TEMPERATURE_DIMENSION] = dimension.name
    }

    val temperatureDimension: Flow<TemperatureDimension> =
        getFlowOf(KEY_TEMPERATURE_DIMENSION, TemperatureDimension.CELCIUS.name).map {
            when (it) { //todo mapping
                "fahrenheit" -> TemperatureDimension.FAHRENHEIT
                else -> TemperatureDimension.CELCIUS
            }
        }

    suspend fun setDarkMode(mode: DarkMode) = edit { preferences ->
        preferences[KEY_DARK_MODE] = mode.name
    }

    val darkMode: Flow<DarkMode> = getFlowOf(KEY_LANGUAGE, DarkMode.SYSTEM.name).map {
        when (it) {//todo mapping
            "ON" -> DarkMode.ON
            "OFF" -> DarkMode.OFF
            else -> DarkMode.SYSTEM
        }
    }

    private suspend inline fun edit(crossinline transform: suspend (MutablePreferences) -> Unit) {
        context.dataStore.edit { preferences ->
            transform(preferences)
        }
    }

    private fun <V> getFlowOf(key: Preferences.Key<V>, defaultValue: V): Flow<V> =
        context.dataStore.data.map { it[key] ?: defaultValue }

    private val Context.dataStore by preferencesDataStore(name = SETTINGS_PREFS)

    companion object {
        private const val SETTINGS_PREFS = "settings"
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_DARK_MODE = stringPreferencesKey("dark_mode")
        private val KEY_DISTANCE_DIMENSION = stringPreferencesKey("distance_dimension")
        private val KEY_TEMPERATURE_DIMENSION = stringPreferencesKey("temperature_dimension")
    }


    //todo refactoring

    enum class Language(lang: String) {
        RU("ru"), EN("en")
    }

    enum class TemperatureDimension(dimensionName: String) {
        FAHRENHEIT("fahrenheit"), CELCIUS("celcius")
    }

    //todo метры в секунду переводить в километры в секунду на стороне мобилы, также с милями
    enum class DistanceDimension(dimensionName: String) {
        METRIC("METRIC"), IMPERIAL("IMPERIAL")
    }

    enum class DarkMode(name: String) {
        ON("ON"), OFF("OFF"), SYSTEM("SYSTEM")
    }

    data class AppSettings(
        val lang: Language,
        val darkMode: DarkMode,
        val distanceDimension: DistanceDimension,
        val temperatureDimension: TemperatureDimension,
    )
}