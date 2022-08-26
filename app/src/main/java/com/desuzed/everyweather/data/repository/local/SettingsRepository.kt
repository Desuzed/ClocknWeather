package com.desuzed.everyweather.data.repository.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = SETTINGS_PREFS)
    private val res = context.resources

    suspend fun setLanguage(lang: Lang) = edit { preferences ->
        preferences[KEY_LANGUAGE] = lang.lang
    }

    val lang: Flow<Language> = getFlowOf(KEY_LANGUAGE, Lang.RU.name).map { langId ->
        val langValueId = when (langId) {
            //todo mapping
            "RU" -> R.string.russian
            else -> R.string.english
        }
        Language(
            id = langId,
            category = res.getString(R.string.language),
            value = res.getString(langValueId),
        )
    }

    suspend fun setDistanceDimension(dimension: DistanceDimen) = edit { preferences ->
        preferences[KEY_DISTANCE_DIMENSION] = dimension.dimensionName
    }

    //todo проверить на каком потоке мапится
    val distanceDimen: Flow<WindSpeed> =
        getFlowOf(KEY_DISTANCE_DIMENSION, DistanceDimen.METRIC.name).map { distanceDimenId ->
            val distValueId = when (distanceDimenId) {
                "IMPERIAL" -> R.string.mph
                else -> R.string.kmh
            }
            WindSpeed(
                id = distanceDimenId,
                category = res.getString(R.string.distance_dimension),
                value = res.getString(distValueId)
            )
        }

    suspend fun setTemperatureDimension(dimension: TempDimen) = edit { preferences ->
        preferences[KEY_TEMPERATURE_DIMENSION] = dimension.dimensionName
    }

    val tempDimen: Flow<Temperature> =
        getFlowOf(KEY_TEMPERATURE_DIMENSION, TempDimen.CELCIUS.name).map { tempDimenId ->
            val tempValueId = when (tempDimenId) { //todo mapping
                "FAHRENHEIT" -> R.string.fahrenheit
                else -> R.string.celcius
            }
            Temperature(
                id = tempDimenId,
                category = res.getString(R.string.temperature_dimension),
                value = res.getString(tempValueId)
            )
        }

    suspend fun setDarkMode(mode: DarkMode) = edit { preferences ->
        preferences[KEY_DARK_MODE] = mode.mode
    }

    val darkMode: Flow<DarkTheme> =
        getFlowOf(KEY_DARK_MODE, DarkMode.SYSTEM.name).map { darkModeId ->
            val darkThemeValueId = when (darkModeId) {//todo mapping
                "ON" -> R.string.on
                "OFF" -> R.string.off
                else -> R.string.system
            }
            DarkTheme(
                id = darkModeId,
                category = res.getString(R.string.dark_mode),
                value = res.getString(darkThemeValueId),

                )
        }

    private suspend inline fun edit(crossinline transform: suspend (MutablePreferences) -> Unit) {
        context.dataStore.edit { preferences ->
            transform(preferences)
        }
    }

    private fun <V> getFlowOf(key: Preferences.Key<V>, defaultValue: V): Flow<V> =
        context.dataStore.data.map { it[key] ?: defaultValue }


    companion object {
        private const val SETTINGS_PREFS = "settings"
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_DARK_MODE = stringPreferencesKey("dark_mode")
        private val KEY_DISTANCE_DIMENSION = stringPreferencesKey("distance_dimension")
        private val KEY_TEMPERATURE_DIMENSION = stringPreferencesKey("temperature_dimension")
    }

}