package com.desuzed.everyweather.presentation.features.main_activity

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.local.SettingsRepository
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import com.desuzed.everyweather.util.NetworkConnection
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val networkConnection: NetworkConnection,
    private val settingsRepository: SettingsRepository,
    private val sharedPrefsProvider: SharedPrefsProvider,
) : BaseViewModel<MainActivityState, MainActivityAction>(MainActivityState()) {

    private val _isLookingForLocation = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val isLookingForLocation: Flow<Boolean> = _isLookingForLocation.asSharedFlow()

    val hasInternet = networkConnection.hasInternetFlow()

    val userLatLng = MutableStateFlow<UserLatLng?>(null)

    private val _messageFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messageFlow: Flow<String> = _messageFlow.asSharedFlow()

    init {
        collect(settingsRepository.lang, ::collectLanguage)
    }

    fun postMessage(message: String) {
        viewModelScope.launch {
            _messageFlow.emit(message)
        }
    }

    fun toggleLookingForLocation(state: Boolean) {
        viewModelScope.launch {
            _isLookingForLocation.emit(state)
        }
    }

    fun onLanguage(appLanguage: String?) {
        viewModelScope.launch {
            val lang = when (appLanguage) {
                "ru" -> Lang.RU
                else -> Lang.EN
            }
            settingsRepository.setLanguage(lang)
        }
    }

    fun isFirstRun() = sharedPrefsProvider.isFirstRunApp()

    private fun collectLanguage(lang: Language) {
        val lowercaseLang = lang.id.lowercase()
        setState { copy(lang = lowercaseLang) }
        setAction(MainActivityAction.ChangeLanguage(lowercaseLang))
    }

}