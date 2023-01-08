package com.desuzed.everyweather.domain.model.settings

data class DarkTheme(
    override val id: String,
    override val categoryStringId: Int,
    override val valueStringId: Int,
    override val type: SettingsType = SettingsType.DARK_MODE,
) : BaseSettingItem()