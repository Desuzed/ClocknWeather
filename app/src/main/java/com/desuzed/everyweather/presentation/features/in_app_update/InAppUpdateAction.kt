package com.desuzed.everyweather.presentation.features.in_app_update

sealed interface InAppUpdateAction {
    object UpdateApplication : InAppUpdateAction
    object InstallUpdate : InAppUpdateAction
    object Dismiss : InAppUpdateAction
}