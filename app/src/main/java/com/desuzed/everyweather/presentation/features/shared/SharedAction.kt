package com.desuzed.everyweather.presentation.features.shared

sealed interface SharedAction {
    object UpdateAvailableDialog : SharedAction
    object UpdateReadyToInstallDialog : SharedAction
}