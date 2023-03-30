package com.desuzed.everyweather.presentation.features.settings

import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus

sealed interface SettingsAction {
    object NavigateBack : SettingsAction
    class ShowUpdateDialog(val status: InAppUpdateStatus) : SettingsAction
    class ShowReadyToInstallDialog(val status: InAppUpdateStatus) : SettingsAction
}