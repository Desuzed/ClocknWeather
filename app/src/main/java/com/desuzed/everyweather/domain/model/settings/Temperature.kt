package com.desuzed.everyweather.domain.model.settings

class Temperature(
    override val id: String,
    override val category: String,
    override val value: String,
    override val type: SettingsType = SettingsType.TEMP,
) : BaseSettingItem()
