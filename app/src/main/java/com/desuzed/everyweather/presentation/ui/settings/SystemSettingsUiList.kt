package com.desuzed.everyweather.presentation.ui.settings

import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.Lang

class SystemSettingsUiList(
    val languageList: List<SettingUiItem<Lang>>,
    val darkModeList: List<SettingUiItem<DarkMode>>,
)