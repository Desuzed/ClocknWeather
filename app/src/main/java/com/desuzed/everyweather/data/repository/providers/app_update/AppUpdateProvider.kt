package com.desuzed.everyweather.data.repository.providers.app_update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.desuzed.everyweather.domain.model.app_update.AppUpdateState
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppUpdateProvider(private val context: Context) {

    private val appUpdateManager = AppUpdateManagerFactory.create(context)
    private val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    private var appUpdateInfo: AppUpdateInfo? = null
    private val _appUpdateState = MutableStateFlow<AppUpdateState?>(null)
    val appUpdateState: Flow<AppUpdateState?> = _appUpdateState.asStateFlow()

    @SuppressLint("SwitchIntDef")
    private val updateListener: InstallStateUpdatedListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                setReadyToInstall()
                setProgressState(
                    progress = 0,
                    totalBytes = 0,
                    isLoading = false,
                )
            }
            InstallStatus.DOWNLOADING -> {
                setProgressState(
                    progress = state.bytesDownloaded(),
                    totalBytes = state.totalBytesToDownload(),
                    isLoading = true,
                )

            }
        }
    }

    fun startListeningForUpdates() {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                setReadyToInstall()
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                this.appUpdateInfo = appUpdateInfo
                setUpdateAvailable()
            }
        }
    }

    fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }

    fun startUpdate(activity: Activity) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo ?: return,
            AppUpdateType.FLEXIBLE,
            activity,
            UPDATE_REQUEST_CODE,
        )
        subscribeOnAppUpdateProgress()
    }

    private fun subscribeOnAppUpdateProgress() {
        appUpdateManager.registerListener(updateListener)
    }

    private fun unsubscribeOnAppUpdate() {
        appUpdateManager.unregisterListener(updateListener)
    }

    private fun setReadyToInstall() {
        _appUpdateState.value = AppUpdateState.ReadyToInstall
    }

    private fun setUpdateAvailable() {
        _appUpdateState.value = AppUpdateState.UpdateAvailable
    }

    private fun setProgressState(progress: Long, totalBytes: Long, isLoading: Boolean) {
        _appUpdateState.value = AppUpdateState.Downloading(
            totalBytes = totalBytes,
            bytesDownloaded = progress,
            isUpdateLoading = isLoading,
        )
    }

    companion object {
        const val UPDATE_REQUEST_CODE = 55
    }
}