package com.desuzed.everyweather.presentation.features.main_activity

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.presentation.base.SideEffect

sealed interface MainActivitySideEffect : SideEffect {
    class ChangeLanguage(val lang: String) : MainActivitySideEffect
    class ChangeDarkMode(val mode: DarkMode) : MainActivitySideEffect
}