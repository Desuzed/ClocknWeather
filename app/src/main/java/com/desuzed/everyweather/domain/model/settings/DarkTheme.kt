package com.desuzed.everyweather.domain.model.settings

class DarkTheme(
    override val id: String,
    override val category: String,
    override val value: String,
    override val type: SettingsType = SettingsType.DARK_MODE
) : BaseSettingItem()