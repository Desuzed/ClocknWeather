package com.desuzed.everyweather.presentation.ui.base

import com.desuzed.everyweather.domain.model.settings.SettingsType

abstract class BaseSettingItem {
    abstract val category: String
    abstract val name: String
    abstract val type: SettingsType
}