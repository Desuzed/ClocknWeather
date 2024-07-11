package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.base.SideEffect

sealed interface SettingsEffect : SideEffect {
    data object NavigateBack : SettingsEffect
    class ShowUpdateDialog(val status: InAppUpdateStatus) : SettingsEffect
    class ShowReadyToInstallDialog(val status: InAppUpdateStatus) : SettingsEffect
}