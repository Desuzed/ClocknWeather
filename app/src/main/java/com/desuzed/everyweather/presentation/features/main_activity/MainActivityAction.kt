package com.desuzed.everyweather.presentation.features.main_activity

import com.desuzed.everyweather.domain.model.settings.DarkMode

sealed interface MainActivityAction {
    class ChangeLanguage(val lang: String) : MainActivityAction
    class ChangeDarkMode(val mode: DarkMode) : MainActivityAction
}