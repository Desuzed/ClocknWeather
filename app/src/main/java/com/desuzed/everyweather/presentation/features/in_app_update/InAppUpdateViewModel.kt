package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.base.BaseViewModel

class InAppUpdateViewModel :
    BaseViewModel<InAppUpdateState, InAppUpdateAction, InAppUpdateUserInteraction>(InAppUpdateState()) {

    fun initState(isReadyToInstall: Boolean) {
        val status = when (isReadyToInstall) {
            true -> InAppUpdateStatus.READY_TO_INSTALL
            false -> InAppUpdateStatus.READY_TO_LAUNCH_UPDATE
        }
        setState { copy(updateStatus = status) }
    }

    override fun onUserInteraction(interaction: InAppUpdateUserInteraction) {
        val action = when (interaction) {
            InAppUpdateUserInteraction.AgreedToUpdate -> InAppUpdateAction.UpdateApplication
            InAppUpdateUserInteraction.Dismiss -> InAppUpdateAction.Dismiss
            InAppUpdateUserInteraction.AgreedToInstallUpdate -> InAppUpdateAction.InstallUpdate
        }
        setAction(action)
    }
}