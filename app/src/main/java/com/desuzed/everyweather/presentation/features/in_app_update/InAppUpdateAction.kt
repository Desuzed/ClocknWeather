package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.presentation.base.Action

sealed interface InAppUpdateAction : Action {
    data object AgreedToUpdate : InAppUpdateAction
    data object AgreedToInstallUpdate : InAppUpdateAction
    data object Dismiss : InAppUpdateAction
}