package com.desuzed.everyweather.domain.interactor

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import kotlinx.coroutines.flow.Flow

class SystemSettingsInteractor(
    private val systemSettingsRepository: SystemSettingsRepository,
) {

    suspend fun setLanguage(lang: Lang) = systemSettingsRepository.setLanguage(lang)

    val lang: Flow<Lang> = systemSettingsRepository.lang

    suspend fun setDarkMode(mode: DarkMode) = systemSettingsRepository.setDarkMode(mode)

    val darkMode: Flow<DarkMode> = systemSettingsRepository.darkMode

}