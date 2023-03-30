package com.desuzed.everyweather.analytics

import android.content.Context
import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateUserInteraction

class InAppUpdateAnalytics(context: Context) : Analytics(context = context) {

    fun onUserInteraction(interaction: InAppUpdateUserInteraction) {
        when (interaction) {
            InAppUpdateUserInteraction.AgreedToInstallUpdate -> logEvent(AGREED_TO_INSTALL_UPDATE)
            InAppUpdateUserInteraction.AgreedToUpdate -> logEvent(AGREED_TO_UPDATE)
            else -> {}
        }
    }

    companion object {
        private const val AGREED_TO_INSTALL_UPDATE = "in_app_update_install"
        private const val AGREED_TO_UPDATE = "in_app_update_start"
    }
}