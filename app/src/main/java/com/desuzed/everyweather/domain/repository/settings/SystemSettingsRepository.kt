package com.desuzed.everyweather.domain.repository.settings

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.Lang
import kotlinx.coroutines.flow.Flow

interface SystemSettingsRepository {

    suspend fun setLanguage(lang: Lang)

    val lang: Flow<Lang>

    suspend fun setDarkMode(mode: DarkMode)

    val darkMode: Flow<DarkMode>

}