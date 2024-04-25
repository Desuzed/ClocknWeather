package com.desuzed.everyweather.domain.repository.settings

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DarkTheme
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import kotlinx.coroutines.flow.Flow

interface SystemSettingsRepository {

    suspend fun setLanguage(lang: Lang)

    val lang: Flow<Language>

    suspend fun setDarkMode(mode: DarkMode)

    val darkMode: Flow<DarkTheme>

    fun getLanguageItemsList(): List<SettingUiItem<Lang>>

    fun getDarkModeItemsList(): List<SettingUiItem<DarkMode>>

}