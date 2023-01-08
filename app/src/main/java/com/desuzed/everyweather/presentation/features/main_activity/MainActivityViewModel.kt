package com.desuzed.everyweather.presentation.features.main_activity

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.local.SettingsDataStore
import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.UserLocationResult
import com.desuzed.everyweather.domain.model.result.ActionResult
import com.desuzed.everyweather.domain.model.settings.DarkMode
import com.desuzed.everyweather.domain.model.settings.DarkTheme
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.presentation.base.UserInteraction
import com.desuzed.everyweather.util.Constants.LANG_RU_LOWERCASE
import com.desuzed.everyweather.util.NetworkConnection
import com.desuzed.everyweather.util.Timer
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MainActivityViewModel(
    networkConnection: NetworkConnection,
    private val settingsDataStore: SettingsDataStore,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val userLocationProvider: UserLocationProvider,
) : BaseViewModel<MainActivityState, MainActivityAction, UserInteraction>(MainActivityState()) {

    private val _isLookingForLocation = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val isLookingForLocation: Flow<Boolean> = _isLookingForLocation.asSharedFlow()

    val hasInternet = networkConnection.hasInternetFlow()

    private val _userLatLng = MutableSharedFlow<UserLatLng?>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val userLatLng: Flow<UserLatLng?> = _userLatLng.asSharedFlow()

    private val _messageFlow = MutableSharedFlow<ActionResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messageFlow: Flow<ActionResult> = _messageFlow.asSharedFlow()

    private var timerJob: Job? = null

    init {
        collect(settingsDataStore.lang, ::collectLanguage)
        collect(settingsDataStore.darkMode, ::collectDarkTheme)
        collect(userLocationProvider.userLocationFlow, ::collectUserLocationResult)
    }

    fun onLanguage(appLanguage: String?) {
        viewModelScope.launch {
            val lang = when (appLanguage) {
                LANG_RU_LOWERCASE -> Lang.RU
                else -> Lang.EN
            }
            settingsDataStore.setLanguage(lang)
        }
    }

    fun findUserLocation() {
        val shouldToggle = userLocationProvider.findUserLocation()
        toggleLookingForLocation(shouldToggle)
        if (shouldToggle) {
            timerJob = viewModelScope.launch {
                Timer.timerFlow(TIMER_DURATION).onCompletion {
                    if (it !is CancellationException) {
                        cancelLookingForLocation()
                    }
                }.collect()
            }
        }
    }

    fun areLocationPermissionsGranted(): Boolean = userLocationProvider.arePermissionsGranted()

    fun isFirstRun() = sharedPrefsProvider.isFirstRunApp()

    private fun cancelLookingForLocation() {
        userLocationProvider.onCancel()
    }

    private fun collectLanguage(lang: Language) {
        val lowercaseLang = lang.id.lowercase()
        setState { copy(lang = lowercaseLang) }
        setAction(MainActivityAction.ChangeLanguage(lowercaseLang))
    }

    private fun collectDarkTheme(theme: DarkTheme) {
        val mode = DarkMode.valueOf(theme.id.uppercase())
        setAction(MainActivityAction.ChangeDarkMode(mode))
    }

    private fun collectUserLocationResult(result: UserLocationResult?) {
        viewModelScope.launch {
            if (result?.userLatLng != null) {
                _userLatLng.emit(result.userLatLng)
            } else if (result?.actionResult != null) {
                postMessage(result.actionResult)
            }
            cancelTimerJob()
            toggleLookingForLocation(false)
        }

    }

    private fun postMessage(actionResult: ActionResult) {
        viewModelScope.launch {
            _messageFlow.emit(actionResult)
        }
    }

    private fun cancelTimerJob() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun toggleLookingForLocation(state: Boolean) {
        viewModelScope.launch {
            _isLookingForLocation.emit(state)
        }
    }

    private companion object {
        private const val TIMER_DURATION = 20
    }

}