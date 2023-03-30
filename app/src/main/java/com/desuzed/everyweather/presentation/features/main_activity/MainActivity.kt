package com.desuzed.everyweather.presentation.features.main_activity

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import androidx.core.view.isVisible
import com.desuzed.everyweather.Config
import com.desuzed.everyweather.R
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider
import com.desuzed.everyweather.databinding.ActivityMainBinding
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.ActionResult
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.presentation.features.in_app_update.InAppUpdateBottomSheet
import com.desuzed.everyweather.presentation.features.shared.SharedAction
import com.desuzed.everyweather.presentation.features.shared.SharedState
import com.desuzed.everyweather.presentation.features.shared.SharedViewModel
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.setAppLocaleAndReturnContext
import com.desuzed.everyweather.util.snackbar
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainActivityViewModel>()
    private val sharedViewModel by viewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Everyweather)//todo поменять сплешскрин на компоуз версию чтобы не видеть белый фон при входе в приложение
        super.onCreate(savedInstanceState)
        handleFirstEnterApp()
        bind()
        collectData()
        sharedViewModel.startListeningForUpdates()
    }

    fun getUserLatLngFlow(): Flow<UserLatLng?> = viewModel.userLatLng

    fun showSnackbar(
        message: String,
        @StringRes actionStringId: Int = R.string.ok,
        onActionClick: () -> Unit = {},
    ) {
        snackbar(
            text = message,
            root = binding.root,
            actionStringId = actionStringId,
            onActionClick = onActionClick,
        )
    }

    fun findUserLocation() {
        viewModel.findUserLocation()
    }

    fun requestLocationPermissions() {
        if (viewModel.areLocationPermissionsGranted()) {
            return
        } else {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    LOCATION_CODE
                )
        }
    }

    private fun changeDarkMode(darkMode: DarkMode) {
        val mode = when (darkMode) {
            DarkMode.ON -> MODE_NIGHT_YES
            DarkMode.OFF -> MODE_NIGHT_NO
            DarkMode.SYSTEM -> MODE_NIGHT_FOLLOW_SYSTEM
        }
        delegate.localNightMode = mode
    }

    private fun handleFirstEnterApp() {
        val isFirstRun = viewModel.isFirstRun()
        if (isFirstRun) {
            initFirstRunLanguage()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val lang = getLocale().language
        viewModel.onLanguage(lang)
        super.onConfigurationChanged(newConfig)
    }

    private fun initFirstRunLanguage() {
        val lang = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]?.language
        viewModel.onLanguage(lang)
    }

    private fun collectData() {
        collect(viewModel.hasInternet, ::onNewNetworkState)
        collect(viewModel.isLookingForLocation, ::isLookingForLocation)
        collect(viewModel.messageFlow, ::onNewActionResult)
        collect(viewModel.action, ::onNewAction)
        collect(sharedViewModel.state, ::onDownloadingUpdateProgress)
        collect(sharedViewModel.action) {
            when (it) {
                SharedAction.UpdateAvailableDialog -> showUpdateDialog(InAppUpdateStatus.READY_TO_LAUNCH_UPDATE)
                SharedAction.UpdateReadyToInstallDialog -> showUpdateDialog(InAppUpdateStatus.READY_TO_INSTALL)
            }
        }
    }

    private fun onNewNetworkState(networkState: Boolean) {
        binding.tvInternetConnection.isVisible = !networkState
    }

    private fun isLookingForLocation(isLooking: Boolean) {
        with(binding) {
            geoLayout.isVisible = isLooking
        }
    }

    private fun showUpdateDialog(status: InAppUpdateStatus) {
        if (supportFragmentManager.findFragmentByTag(IN_APP_UPDATE_DIALOG_TAG) == null) {
            InAppUpdateBottomSheet().apply {
                setUpdateStatus(status)
                show(supportFragmentManager, IN_APP_UPDATE_DIALOG_TAG)
            }
        }
    }

    private fun onNewActionResult(actionResult: ActionResult) {
        val onClick: () -> Unit
        val buttonTextId: Int
        when (actionResult.actionType) {
            ActionType.OK -> {
                onClick = {}
                buttonTextId = R.string.ok
            }
            ActionType.RETRY -> {
                buttonTextId = R.string.retry
                onClick = {
                    findUserLocation()
                }
            }
        }
        val provider = GeoActionResultProvider(resources)
        val message = provider.parseCode(errorCode = actionResult.code)
        showSnackbar(
            message = message,
            actionStringId = buttonTextId,
            onActionClick = onClick,
        )
    }

    private fun onNewAction(action: MainActivityAction) {
        when (action) {
            is MainActivityAction.ChangeLanguage -> changeAppLanguage(action.lang)
            is MainActivityAction.ChangeDarkMode -> changeDarkMode(action.mode)
        }
    }

    private fun changeAppLanguage(lang: String) {
        val locale = Locale(lang)
        val currentLocale = getLocale()
        if (locale == currentLocale || lang.isEmpty()) {
            return
        }
        Config.lang = lang
        recreate()
    }

    private fun bind() {
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = binding.root
        setContentView(view)
    }

    private fun getLocale(): Locale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }


    override fun attachBaseContext(newBase: Context) {
        val language = Config.lang
        val newContext = if (language.isNotBlank()) {
            setAppLocaleAndReturnContext(language, newBase)
        } else {
            newBase
        }
        super.attachBaseContext(newContext)
    }

    private fun onDownloadingUpdateProgress(sharedState: SharedState) {
        with(binding) {
            appUpdateLayout.isVisible = sharedState.isUpdateLoading
            appUpdateProgressBar.max = sharedState.totalBytes.toInt()
            appUpdateProgressBar.progress = sharedState.bytesDownloaded.toInt()
        }
    }

    companion object {
        private const val IN_APP_UPDATE_DIALOG_TAG = "IN_APP_UPDATE_DIALOG_TAG"
        private const val LOCATION_CODE = 100
    }

}
