package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateAction

class InAppUpdateAnalytics(context: Context) : Analytics(context = context) {

    fun onAction(action: InAppUpdateAction) {
        when (action) {
            InAppUpdateAction.AgreedToInstallUpdate -> logEvent(AGREED_TO_INSTALL_UPDATE)
            InAppUpdateAction.AgreedToUpdate -> logEvent(AGREED_TO_UPDATE)
            else -> {}
        }
    }

    companion object {
        private const val AGREED_TO_INSTALL_UPDATE = "in_app_update_install"
        private const val AGREED_TO_UPDATE = "in_app_update_start"
    }
}