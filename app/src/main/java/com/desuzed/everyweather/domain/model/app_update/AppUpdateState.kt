package com.desuzed.everyweather.domain.model.app_update

sealed interface AppUpdateState {
    data class Downloading(
        val totalBytes: Long,
        val bytesDownloaded: Long,
        val isUpdateLoading: Boolean,
    ) : AppUpdateState

    object UpdateAvailable : AppUpdateState
    object ReadyToInstall : AppUpdateState
}