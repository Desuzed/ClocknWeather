package com.desuzed.everyweather.presentation.features.shared

import com.desuzed.everyweather.presentation.base.SideEffect

sealed interface SharedEffect : SideEffect {
    data object UpdateAvailableDialog : SharedEffect
    data object UpdateReadyToInstallDialog : SharedEffect
}