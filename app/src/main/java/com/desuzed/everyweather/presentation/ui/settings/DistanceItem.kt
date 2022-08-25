package com.desuzed.everyweather.presentation.ui.settings

import android.content.res.Resources
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.presentation.ui.base.BaseSettingItem

class DistanceItem (res: Resources): BaseSettingItem() {
    override val category: String
    override val name: String
    override val type: SettingsType = SettingsType.DISTANCE

    init {
        //todo constructor
        category = res.getString(R.string.distance_dimension)
        name = res.getString(R.string.distance_dimension)
    }
}
