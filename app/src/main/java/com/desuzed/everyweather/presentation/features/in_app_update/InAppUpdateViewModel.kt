package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.analytics.InAppUpdateAnalytics
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.base.BaseViewModel

class InAppUpdateViewModel(
    private val analytics: InAppUpdateAnalytics,
) :
    BaseViewModel<InAppUpdateState, InAppUpdateAction, InAppUpdateUserInteraction>(InAppUpdateState()) {

    override fun onUserInteraction(interaction: InAppUpdateUserInteraction) {
        analytics.onUserInteraction(interaction)
        val action = when (interaction) {
            InAppUpdateUserInteraction.Dismiss -> InAppUpdateAction.Dismiss
            InAppUpdateUserInteraction.AgreedToInstallUpdate -> InAppUpdateAction.InstallUpdate
            InAppUpdateUserInteraction.AgreedToUpdate -> InAppUpdateAction.UpdateApplication
        }
        setAction(action)
    }

    fun resolveStatus(status: InAppUpdateStatus) {
        setState { copy(updateStatus = status) }
    }
}