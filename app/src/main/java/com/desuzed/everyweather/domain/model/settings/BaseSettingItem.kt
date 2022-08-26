package com.desuzed.everyweather.domain.model.settings

abstract class BaseSettingItem {
    abstract val id: String
    abstract val categoryStringId: Int
    abstract val valueStringId: Int
    abstract val type: SettingsType
}