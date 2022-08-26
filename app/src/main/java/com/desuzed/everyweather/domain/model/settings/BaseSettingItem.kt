package com.desuzed.everyweather.domain.model.settings

abstract class BaseSettingItem {
    abstract val id: String
    abstract val category: String
    abstract val value: String
    abstract val type: SettingsType
}