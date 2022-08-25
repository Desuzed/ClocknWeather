package com.desuzed.everyweather.presentation.ui.settings

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.presentation.ui.base.BaseSettingItem

class DarkModeItem (res: Resources): BaseSettingItem() {
    override val category: String
    override val name: String
    override val type: SettingsType = SettingsType.DARK_MODE

    init {
        //todo constructor
        category = res.getString(R.string.dark_mode)
        name = res.getString(R.string.dark_mode)
    }
}
