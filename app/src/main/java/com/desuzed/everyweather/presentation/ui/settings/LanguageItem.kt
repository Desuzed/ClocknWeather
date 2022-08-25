package com.desuzed.everyweather.presentation.ui.settings

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.presentation.ui.base.BaseSettingItem

class LanguageItem (res: Resources): BaseSettingItem() {
    override val category: String
    override val name: String
    override val type: SettingsType = SettingsType.LANG

    init {
        //todo constructor
        category = res.getString(R.string.language)
        name = res.getString(R.string.language)
    }
}
