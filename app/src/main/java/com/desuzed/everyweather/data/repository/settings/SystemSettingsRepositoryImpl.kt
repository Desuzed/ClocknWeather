package com.desuzed.everyweather.data.repository.settings

import androidx.datastore.preferences.core.stringPreferencesKey
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.repository.settings.DatastoreApiProvider
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SystemSettingsRepositoryImpl(private val datastoreProvider: DatastoreApiProvider) :
    SystemSettingsRepository {

    override suspend fun setLanguage(lang: Lang) = datastoreProvider.edit { preferences ->
        preferences[KEY_LANGUAGE] = lang.lang
    }

    override val lang: Flow<Lang> =
        datastoreProvider.getFlowOf(KEY_LANGUAGE, Lang.RU.lang).map { langStrValue ->
            when (langStrValue) {
                Lang.RU.lang -> Lang.RU
                else -> Lang.EN
            }
        }

    override suspend fun setDarkMode(mode: DarkMode) = datastoreProvider.edit { preferences ->
        preferences[KEY_DARK_MODE] = mode.mode
    }

    override val darkMode: Flow<DarkMode> =
        datastoreProvider.getFlowOf(KEY_DARK_MODE, DarkMode.SYSTEM.mode).map { darkModeStrValue ->
            when (darkModeStrValue) {
                DarkMode.ON.mode -> DarkMode.ON
                DarkMode.OFF.mode -> DarkMode.OFF
                else -> DarkMode.SYSTEM
            }
        }

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_DARK_MODE = stringPreferencesKey("dark_mode")
    }

}