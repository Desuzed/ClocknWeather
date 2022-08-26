package com.desuzed.everyweather.domain.model.settings

class Language(
    override val id: String,
    override val categoryStringId: Int,
    override val valueStringId: Int,
    override val type: SettingsType = SettingsType.LANG,
) : BaseSettingItem()