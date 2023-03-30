package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.presentation.base.UserInteraction

sealed interface InAppUpdateUserInteraction : UserInteraction {
    object AgreedToUpdate : InAppUpdateUserInteraction
    object AgreedToInstallUpdate : InAppUpdateUserInteraction
    object Dismiss : InAppUpdateUserInteraction
}