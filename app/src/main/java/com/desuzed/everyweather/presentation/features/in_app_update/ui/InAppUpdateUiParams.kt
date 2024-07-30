package com.desuzed.everyweather.presentation.features.in_app_update.ui

import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateAction

class InAppUpdateUiParams private constructor(
    val titleId: Int,
    val descriptionId: Int,
    val negativeButtonTextId: Int,
    val positiveButtonTextId: Int,
    val positiveButtonInteraction: InAppUpdateAction
) {

    companion object {

        fun fromInAppUpdateStatus(status: InAppUpdateStatus): InAppUpdateUiParams {
            return when (status) {
                InAppUpdateStatus.READY_TO_LAUNCH_UPDATE -> InAppUpdateUiParams(
                    titleId = R.string.update_available_title,
                    descriptionId = R.string.update_available_description,
                    negativeButtonTextId = R.string.update_available_later_button,
                    positiveButtonTextId = R.string.update_available_update_button,
                    positiveButtonInteraction = InAppUpdateAction.AgreedToUpdate,
                )

                InAppUpdateStatus.READY_TO_INSTALL -> InAppUpdateUiParams(
                    titleId = R.string.update_ready_to_install_title,
                    descriptionId = R.string.should_install_update_description,
                    negativeButtonTextId = R.string.update_dismiss_button,
                    positiveButtonTextId = R.string.update_install_button,
                    positiveButtonInteraction = InAppUpdateAction.AgreedToInstallUpdate,
                )
            }
        }
    }
}