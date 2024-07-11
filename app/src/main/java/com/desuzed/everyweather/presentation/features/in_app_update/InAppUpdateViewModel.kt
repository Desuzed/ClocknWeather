package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.analytics.InAppUpdateAnalytics
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.base.BaseViewModel

class InAppUpdateViewModel(
    private val analytics: InAppUpdateAnalytics,
) :
    BaseViewModel<InAppUpdateState, InAppUpdateEffect, InAppUpdateUserInteraction>(InAppUpdateState()) {

    override fun onUserInteraction(interaction: InAppUpdateUserInteraction) {
        analytics.onUserInteraction(interaction)
        val action = when (interaction) {
            InAppUpdateUserInteraction.Dismiss -> InAppUpdateEffect.Dismiss
            InAppUpdateUserInteraction.AgreedToInstallUpdate -> InAppUpdateEffect.InstallUpdate
            InAppUpdateUserInteraction.AgreedToUpdate -> InAppUpdateEffect.UpdateApplication
        }
        setSideEffect(action)
    }

    fun resolveStatus(status: InAppUpdateStatus) {
        setState { copy(updateStatus = status) }
    }
}