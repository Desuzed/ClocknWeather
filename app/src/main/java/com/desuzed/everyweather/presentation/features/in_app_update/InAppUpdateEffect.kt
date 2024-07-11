package com.desuzed.everyweather.presentation.features.in_app_update

import com.desuzed.everyweather.presentation.base.SideEffect

sealed interface InAppUpdateEffect : SideEffect {
    data object UpdateApplication : InAppUpdateEffect
    data object InstallUpdate : InAppUpdateEffect
    data object Dismiss : InAppUpdateEffect
}