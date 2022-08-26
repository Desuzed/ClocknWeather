package com.desuzed.everyweather.domain.model.settings

class WindSpeed(
    override val id: String,
    override val category: String,
    override val value: String,
    override val type: SettingsType = SettingsType.DISTANCE
) : BaseSettingItem()
