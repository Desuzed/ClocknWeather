package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.Language


sealed interface SettingsAction{
    class ChangeLanguage(val language: Language) : SettingsAction
    class ChangeDarkMode(val mode: DarkMode) : SettingsAction
    object NavigateBack: SettingsAction
}