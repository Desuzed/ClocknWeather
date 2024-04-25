package com.desuzed.everyweather.domain.interactor

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DarkTheme
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.SettingUiItem
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import kotlinx.coroutines.flow.Flow

class SystemSettingsInteractor(
    private val systemSettingsRepository: SystemSettingsRepository,
) {

    suspend fun setLanguage(lang: Lang) = systemSettingsRepository.setLanguage(lang)

    val lang: Flow<Language> = systemSettingsRepository.lang

    suspend fun setDarkMode(mode: DarkMode) = systemSettingsRepository.setDarkMode(mode)

    val darkMode: Flow<DarkTheme> = systemSettingsRepository.darkMode

    fun getLanguageItemsList(): List<SettingUiItem<Lang>> =
        systemSettingsRepository.getLanguageItemsList()

    fun getDarkModeItemsList(): List<SettingUiItem<DarkMode>> =
        systemSettingsRepository.getDarkModeItemsList()
}