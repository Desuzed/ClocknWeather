package com.desuzed.everyweather.presentation.features.shared

import android.app.Activity
import com.desuzed.everyweather.data.repository.providers.app_update.AppUpdateProvider
import com.desuzed.everyweather.domain.model.app_update.AppUpdateState
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.base.UserInteraction
import com.desuzed.everyweather.util.Constants.ZERO_LONG

//TODO избавиться от общей вью модели
class SharedViewModel(
    private val appUpdateProvider: AppUpdateProvider,
) : BaseViewModel<SharedState, SharedAction, UserInteraction>(SharedState()) {

    init {
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
                totalBytes = ZERO_LONG,
                bytesDownloaded = ZERO_LONG,
                isUpdateLoading = false,
            )
        }
    }

    private fun showUpdateAvailableDialog() {
        setAction(SharedAction.UpdateAvailableDialog)
    }

}