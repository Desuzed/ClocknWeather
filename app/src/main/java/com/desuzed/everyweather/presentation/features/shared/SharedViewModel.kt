package com.desuzed.everyweather.presentation.features.shared

import android.app.Activity
import android.util.Log
import com.desuzed.everyweather.data.repository.providers.app_update.AppUpdateProvider
import com.desuzed.everyweather.domain.model.app_update.AppUpdateState
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.base.UserInteraction

class SharedViewModel(
    private val appUpdateProvider: AppUpdateProvider,
) : BaseViewModel<SharedState, SharedAction, UserInteraction>(SharedState(0, 0, false)) {

    init {
//        val c = 30_000_000L
//        launch {
//            for (i: Long in 1..c step 10_0000) {
//                onDownloadingUpdateProgress(AppUpdateState.Downloading(c, i, true))
//                delay(10)
//            }
//            updateReadyToComplete()
//        }
        collect(appUpdateProvider.appUpdateState, ::onAppUpdateState)
    }

    fun startListeningForUpdates() {
        appUpdateProvider.startListeningForUpdates()
    }

    fun completeUpdate() {
        appUpdateProvider.completeUpdate()
    }

    fun startUpdate(activity: Activity) {
        appUpdateProvider.startUpdate(activity)
    }

    private fun showUpdateReadyToInstallDialog() {
        setAction(SharedAction.UpdateReadyToInstallDialog)
    }

    private fun onAppUpdateState(appUpdateState: AppUpdateState?) {
        Log.e("UPDATE", "onAppUpdateState: appUpdateState = $appUpdateState")

        when (appUpdateState) {
            is AppUpdateState.Downloading -> onDownloadingUpdateProgress(appUpdateState)
            AppUpdateState.ReadyToInstall -> updateReadyToComplete()
            AppUpdateState.UpdateAvailable -> showUpdateAvailableDialog()
            null -> {}
        }
    }

    private fun onDownloadingUpdateProgress(state: AppUpdateState.Downloading) {
        setState {
            copy(
                totalBytes = state.totalBytes,
                bytesDownloaded = state.bytesDownloaded,
                isUpdateLoading = state.isUpdateLoading,
            )
        }
    }

    private fun updateReadyToComplete() {
        showUpdateReadyToInstallDialog()
        setState {
            copy(
                totalBytes = 0,
                bytesDownloaded = 0,
                isUpdateLoading = false,
            )
        }
    }

    private fun showUpdateAvailableDialog() {
        setAction(SharedAction.UpdateAvailableDialog)
    }

}