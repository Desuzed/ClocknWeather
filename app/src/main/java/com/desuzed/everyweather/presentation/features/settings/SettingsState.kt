package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.settings.*
import com.desuzed.everyweather.presentation.ui.base.BaseSettingItem

data class SettingsState(
    val language: Language = Language.RU,
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val distanceDimension: DistanceDimension = DistanceDimension.METRIC,
    val temperatureDimension: TemperatureDimension = TemperatureDimension.CELCIUS,
    val uiItems: List<BaseSettingItem> = emptyList(),
    val showDialogType: SettingsType? = null,
)
