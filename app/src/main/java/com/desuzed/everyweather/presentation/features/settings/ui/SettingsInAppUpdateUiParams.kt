package com.desuzed.everyweather.presentation.features.settings.ui

import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.settings.SettingsUserInteraction

class SettingsInAppUpdateUiParams private constructor(
    val userInteraction: SettingsUserInteraction,
    val titleTextId: Int,
    val buttonTextId: Int,
) {

    companion object {
        fun fromInAppUpdateStatus(status: InAppUpdateStatus): SettingsInAppUpdateUiParams {
            return when (status) {
                InAppUpdateStatus.READY_TO_LAUNCH_UPDATE -> SettingsInAppUpdateUiParams(
                    userInteraction = SettingsUserInteraction.ReadyToLaunchUpdate,
                    titleTextId = R.string.update_available_title,
                    buttonTextId = R.string.update_available_update_in_background_button,
                )

                InAppUpdateStatus.READY_TO_INSTALL -> SettingsInAppUpdateUiParams(
                    userInteraction = SettingsUserInteraction.ReadyToInstall,
                    titleTextId = R.string.update_ready_to_install_title,
                    buttonTextId = R.string.update_install_button,
                )
            }
        }
    }
}