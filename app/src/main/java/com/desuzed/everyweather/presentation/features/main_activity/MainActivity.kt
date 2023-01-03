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
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.ActionResult
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.setAppLocaleAndReturnContext
import com.desuzed.everyweather.util.snackbar
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val locationCode = 100
    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Everyweather)//todo поменять сплешскрин на компоуз версию чтобы не видеть белый фон при входе в приложение
        super.onCreate(savedInstanceState)
        handleFirstEnterApp()
        bind()
        collectData()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        findUserLocation()
    }

    fun getUserLatLngFlow(): Flow<UserLatLng?> = viewModel.userLatLng

    fun showSnackbar(
        message: String,
        @StringRes actionStringId: Int = R.string.ok,
        onActionClick: () -> Unit = {}
    ) {
        snackbar(
            text = message,
            root = binding.root,
            actionStringId = actionStringId,
            onActionClick = onActionClick
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
                    locationCode
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
    }

    private fun onNewNetworkState(networkState: Boolean) {
        binding.tvInternetConnection.isVisible = !networkState
    }

    private fun isLookingForLocation(isLooking: Boolean) {
        with(binding) {
            geoLayout.isVisible = isLooking
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

}
