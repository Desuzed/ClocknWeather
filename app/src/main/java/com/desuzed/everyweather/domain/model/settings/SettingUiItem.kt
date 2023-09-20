package com.desuzed.everyweather.domain.model.settings

class SettingUiItem<S : Enum<*>>(
    val setting: S,
    val textId: Int,
)