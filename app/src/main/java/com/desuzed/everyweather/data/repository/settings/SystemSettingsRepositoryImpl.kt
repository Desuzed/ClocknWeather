package com.desuzed.everyweather.data.repository.settings

import androidx.datastore.preferences.core.stringPreferencesKey
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DarkTheme
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import com.desuzed.everyweather.domain.repository.settings.DatastoreApiProvider
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SystemSettingsRepositoryImpl(private val datastoreProvider: DatastoreApiProvider) :
    SystemSettingsRepository {

    override suspend fun setLanguage(lang: Lang) = datastoreProvider.edit { preferences ->
        preferences[KEY_LANGUAGE] = lang.lang
    }

    override val lang: Flow<Language> =
        datastoreProvider.getFlowOf(KEY_LANGUAGE, Lang.RU.lang).map { langId ->
            val langValueId = when (langId) {
                Lang.RU.lang -> R.string.russian
                else -> R.string.english
            }
            Language(
                id = langId,
                categoryStringId = R.string.language,
                valueStringId = langValueId,
            )
        }

    override suspend fun setDarkMode(mode: DarkMode) = datastoreProvider.edit { preferences ->
        preferences[KEY_DARK_MODE] = mode.mode
    }

    override val darkMode: Flow<DarkTheme> =
        datastoreProvider.getFlowOf(KEY_DARK_MODE, DarkMode.SYSTEM.mode).map { darkModeId ->
            val darkThemeValueId = when (darkModeId) {
                DarkMode.ON.mode -> R.string.on
                DarkMode.OFF.mode -> R.string.off
                else -> R.string.system
            }
            DarkTheme(
                id = darkModeId,
                categoryStringId = R.string.dark_mode,
                valueStringId = darkThemeValueId,
            )
        }

    override fun getLanguageItemsList(): List<SettingUiItem<Lang>> {
        return Lang.values().toList().map {
            when (it) {
                Lang.RU -> SettingUiItem(it, R.string.russian)
                Lang.EN -> SettingUiItem(it, R.string.english)
            }
        }
    }


    override fun getDarkModeItemsList(): List<SettingUiItem<DarkMode>> {
        return DarkMode.values().toList().map {
            when (it) {
                DarkMode.ON -> SettingUiItem(it, R.string.on)
                DarkMode.OFF -> SettingUiItem(it, R.string.off)
                DarkMode.SYSTEM -> SettingUiItem(it, R.string.system)
            }
        }
    }

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_DARK_MODE = stringPreferencesKey("dark_mode")
    }

}