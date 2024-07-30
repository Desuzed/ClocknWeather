package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.analytics.InAppUpdateAnalytics
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.base.BaseViewModel

class InAppUpdateViewModel(
    private val analytics: InAppUpdateAnalytics,
) :
    BaseViewModel<InAppUpdateState, InAppUpdateEffect, InAppUpdateAction>(InAppUpdateState()) {

    override fun onAction(action: InAppUpdateAction) {
        analytics.onAction(action)
        val action = when (action) {
            InAppUpdateAction.Dismiss -> InAppUpdateEffect.Dismiss
            InAppUpdateAction.AgreedToInstallUpdate -> InAppUpdateEffect.InstallUpdate
            InAppUpdateAction.AgreedToUpdate -> InAppUpdateEffect.UpdateApplication
        }
        setSideEffect(action)
    }

    fun resolveStatus(status: InAppUpdateStatus) {
        setState { copy(updateStatus = status) }
    }
}