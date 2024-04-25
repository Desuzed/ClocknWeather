package com.desuzed.everyweather.presentation.features.settings

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.analytics.SettingsAnalytics
import com.desuzed.everyweather.data.repository.providers.app_update.AppUpdateProvider
import com.desuzed.everyweather.domain.interactor.WeatherSettingsInteractor
import com.desuzed.everyweather.domain.model.app_update.AppUpdateState
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DarkTheme
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.SettingsType
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val weatherSerringsInteractor: WeatherSettingsInteractor,
    private val systemSettingsRepository: SystemSettingsRepository,
    private val analytics: SettingsAnalytics,
    private val appUpdateProvider: AppUpdateProvider,
) : BaseViewModel<SettingsState, SettingsAction, SettingsUserInteraction>(SettingsState()) {

    init {
        collect(systemSettingsRepository.darkMode, ::collectDarkTheme)
        collect(systemSettingsRepository.lang, ::collectLanguage)
        collect(weatherSerringsInteractor.distanceDimen, ::collectWindSpeed)
        collect(weatherSerringsInteractor.tempDimen, ::collectTemperature)
        collect(weatherSerringsInteractor.pressureDimen, ::collectPressure)
        collect(appUpdateProvider.appUpdateState, ::onAppUpdateState)
        initSettingItemsLists()
    }

    override fun onUserInteraction(interaction: SettingsUserInteraction) {
        analytics.onUserInteraction(interaction)
        when (interaction) {
            is SettingsUserInteraction.ChangeLanguage -> onLanguage(interaction.lang)
            is SettingsUserInteraction.ChangeDarkMode -> onDarkMode(interaction.darkMode)
            is SettingsUserInteraction.ChangeDistanceDimension -> onDistanceDimen(interaction.distanceDimen)
            is SettingsUserInteraction.ChangeTemperatureDimension -> onTemperatureDimen(interaction.tempDimen)
            is SettingsUserInteraction.ChangePressureDimension -> onPressureDimen(interaction.pressureDimen)
            is SettingsUserInteraction.ShowSettingDialog -> showSettingsDialog(interaction.type)
            SettingsUserInteraction.DismissDialog -> hideDialog()
            SettingsUserInteraction.OnBackClick -> setAction(SettingsAction.NavigateBack)
            SettingsUserInteraction.ReadyToLaunchUpdate -> showUpdateDialog()
            SettingsUserInteraction.ReadyToInstall -> showInstallDialog()
        }
    }

    private fun showSettingsDialog(type: SettingsType) {
        setState { copy(showDialogType = type) }
    }

    private fun hideDialog() {
        setState { copy(showDialogType = null) }
    }

    private fun showUpdateDialog() {
        setAction(
            SettingsAction.ShowUpdateDialog(InAppUpdateStatus.READY_TO_LAUNCH_UPDATE)
        )
    }

    private fun showInstallDialog() {
        setAction(
            SettingsAction.ShowReadyToInstallDialog(InAppUpdateStatus.READY_TO_INSTALL)
        )
    }

    private fun onDarkMode(darkMode: DarkMode) {
        viewModelScope.launch {
            hideDialog()
            systemSettingsRepository.setDarkMode(darkMode)
        }
    }

    private fun onLanguage(lang: Lang) {
        viewModelScope.launch {
            hideDialog()
            systemSettingsRepository.setLanguage(lang)
        }
    }

    private fun onDistanceDimen(distanceDimen: DistanceDimen) {
        viewModelScope.launch {
            weatherSerringsInteractor.setDistanceDimension(distanceDimen)
            delay(DELAY_500_MS)
            hideDialog()
        }
    }

    private fun onTemperatureDimen(tempDimen: TempDimen) {
        viewModelScope.launch {
            weatherSerringsInteractor.setTemperatureDimension(tempDimen)
            delay(DELAY_500_MS)
            hideDialog()
        }
    }

    private fun onPressureDimen(pressureDimen: PressureDimen) {
        viewModelScope.launch {
            weatherSerringsInteractor.setPressureDimension(pressureDimen)
            delay(DELAY_500_MS)
            hideDialog()
        }
    }

    private fun collectDarkTheme(darkTheme: DarkTheme) = setState {
        copy(darkTheme = darkTheme)
    }

    private fun collectLanguage(language: Language) = setState {
        copy(lang = language)
    }

    private fun collectWindSpeed(windSpeed: WindSpeed) = setState {
        copy(windSpeed = windSpeed)
    }

    private fun collectTemperature(temperature: Temperature) = setState {
        copy(tempDimen = temperature)
    }

    private fun collectPressure(pressure: Pressure) = setState {
        copy(pressure = pressure)
    }

    private fun initSettingItemsLists() {
        setState {
            copy(
                langDialogItems = systemSettingsRepository.getLanguageItemsList(),
                darkModeDialogItems = systemSettingsRepository.getDarkModeItemsList(),
                temperatureDialogItems = weatherSerringsInteractor.getTemperatureItemsList(),
                distanceDialogItems = weatherSerringsInteractor.getDistanceItemsList(),
                pressureDialogItems = weatherSerringsInteractor.getPressureItemsList(),
            )
        }
    }

    private fun onAppUpdateState(appUpdateState: AppUpdateState?) {
        when (appUpdateState) {
            AppUpdateState.UpdateAvailable -> {
                setState { copy(updateStatus = InAppUpdateStatus.READY_TO_LAUNCH_UPDATE) }
            }

            AppUpdateState.ReadyToInstall -> {
                setState { copy(updateStatus = InAppUpdateStatus.READY_TO_INSTALL) }
            }

            else -> {
                setState { copy(updateStatus = null) }
            }
        }
    }

    private companion object {
        private const val DELAY_500_MS = 500L
    }

}