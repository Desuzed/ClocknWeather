package com.desuzed.everyweather.domain.model.settings

class Language(
    override val id: String,
    override val category: String,
    override val value: String,
    override val type: SettingsType = SettingsType.LANG,
) : BaseSettingItem()