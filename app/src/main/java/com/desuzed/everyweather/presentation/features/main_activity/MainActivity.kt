package com.desuzed.everyweather.presentation.features.main_activity

import android.Manifest
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import androidx.core.view.isVisible
import com.desuzed.everyweather.R
import com.desuzed.everyweather.databinding.ActivityMainBinding
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.util.LocationHandler
import com.desuzed.everyweather.util.collect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val locationHandler by lazy { LocationHandler(this, viewModel) }
    private val locationCode = 100
    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Everyweather)//todo поменять сплешскрин на компоуз версию чтобы не видеть белый фон при входе в приложение
        super.onCreate(savedInstanceState)
        handleFirstEnterApp()
        bind()
        requestLocationPermissions()
        collectData()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.toggleLookingForLocation(true)
        locationHandler.findUserLocation()
    }

    //todo рефакторинг на нормальное взаимодействие
    fun getUserLatLngFlow(): SharedFlow<UserLatLng?> = viewModel.userLatLng.asSharedFlow()

    private fun requestLocationPermissions() {
        //if (locationHandler.permissionsGranted()) return
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

    private fun handleFirstEnterApp() {
        val isFirstRun = viewModel.isFirstRun()
        if (isFirstRun) {
            initFirstRunLanguage()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val lang = newConfig.locales[0].language
        viewModel.onLanguage(lang)
        super.onConfigurationChanged(newConfig)
    }

    private fun initFirstRunLanguage() {
        val lang = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0].language
        viewModel.onLanguage(lang)
    }

    private fun collectData() {
        collect(viewModel.hasInternet, ::onNewNetworkState)
        collect(viewModel.isLookingForLocation, ::isLookingForLocation)
        collect(viewModel.messageFlow, ::onNewMessage)
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

    private fun onNewMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onNewAction(action: MainActivityAction) {
        when (action) {
            is MainActivityAction.ChangeLanguage -> changeAppLanguage(action.lang)
        }
    }

    //todo refactor deprecated
    private fun changeAppLanguage(lang: String) {
        val locale = Locale(lang)
        resources.configuration.setLocale(locale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }

    private fun bind() {
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = binding.root
        setContentView(view)
    }

}
