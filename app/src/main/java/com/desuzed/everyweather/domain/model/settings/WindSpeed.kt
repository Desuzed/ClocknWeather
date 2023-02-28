package com.desuzed.everyweather.domain.model.settings

data class WindSpeed(
    override val id: String,
    override val categoryStringId: Int,
    override val valueStringId: Int,
    override val type: SettingsType = SettingsType.DISTANCE
) : BaseSettingItem()
