package com.desuzed.everyweather.presentation.features.weather_main

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.analytics.WeatherMainAnalytics
import com.desuzed.everyweather.data.repository.local.SettingsDataStore
import com.desuzed.everyweather.data.repository.providers.action_result.ActionResultProvider
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.ActionResult
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherResponse
import com.desuzed.everyweather.domain.repository.local.RoomProvider
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class WeatherMainViewModel(
    private val weatherRepository: WeatherRepository,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val actionResultProvider: ActionResultProvider,
    private val roomProvider: RoomProvider,
    private val analytics: WeatherMainAnalytics,
    settingsDataStore: SettingsDataStore,
) :
    BaseViewModel<WeatherState, WeatherMainAction>(WeatherState()) {

    private val actionResultFlow = MutableSharedFlow<ActionResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        getCachedForecast()
        loadCachedQuery()

        collect(settingsDataStore.distanceDimen, ::collectWindSpeed)
        collect(settingsDataStore.tempDimen, ::collectTemperature)
        collect(settingsDataStore.lang, ::collectLanguage)
        collect(settingsDataStore.pressureDimen, ::collectPressure)
        collect(actionResultFlow, ::collectActionResult)
    }

    fun getForecast(query: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, query = query) }
            val fetchedForecast =
                weatherRepository.fetchForecastOrErrorMessage(
                    query,
                    state.value.lang.id.lowercase()
                )
            val weatherResponse = fetchedForecast.weatherResponse
            val actionResult = fetchedForecast.actionResult
            val isLocationSaved = weatherResponse?.let { isLocationSaved(it) }
            setState {
                copy(
                    weatherData = weatherResponse,
                    isLoading = false,
                    isAddButtonEnabled = isLocationSaved?.not() ?: false,
                )
            }
            if (actionResult != null) {
                actionResultFlow.emit(actionResult)
                setState {
                    copy(
                        isLoading = false,
                        isAddButtonEnabled = false,
                    )
                }
            }
        }
    }

    fun onUserInteraction(userInteraction: WeatherUserInteraction) {
        analytics.onUserInteraction(userInteraction)
        when (userInteraction) {
            WeatherUserInteraction.Location -> setAction(WeatherMainAction.NavigateToLocation)
            WeatherUserInteraction.NextDays -> setAction(WeatherMainAction.NavigateToNextDaysWeather)
            WeatherUserInteraction.Refresh -> getForecast(state.value.query)
            WeatherUserInteraction.SaveLocation -> saveLocation()
            is WeatherUserInteraction.Redirection -> setAction(
                WeatherMainAction.ShowSnackbar(
                    ActionResult(message = userInteraction.message)
                )
            )
        }
    }

    private fun saveLocation() {
        viewModelScope.launch {
            if (state.value.weatherData == null) {
                onError(ActionResultProvider.FAIL)
                return@launch
            }
            val favoriteLocationDto =
                FavoriteLocationDto.buildFavoriteLocationObj(state.value.weatherData!!.location)
            val inserted = roomProvider.insert(favoriteLocationDto)
            if (inserted) onSuccess(ActionResultProvider.SAVED)
            else onError(ActionResultProvider.FAIL)
        }
    }

    private fun onError(code: Int) {
        viewModelScope.launch {
            val message = actionResultProvider.parseCode(code)
            actionResultFlow.emit(message)
        }
    }

    private fun onSuccess(code: Int) {
        val message = actionResultProvider.parseCode(code)
        viewModelScope.launch {
            actionResultFlow.emit(message)
            setState {
                copy(isAddButtonEnabled = false)
            }
        }

    }

    private suspend fun isLocationSaved(response: WeatherResponse): Boolean {
        val latLonKey = FavoriteLocationDto.generateKey(response.location)
        return roomProvider.containsPrimaryKey(latLonKey)
    }

    private fun getCachedForecast() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val result = sharedPrefsProvider.loadForecastFromCache()
            if (result != null) {
                val isLocationSaved = isLocationSaved(result)
                setState {
                    copy(
                        isAddButtonEnabled = !isLocationSaved,
                        weatherData = result,
                    )
                }
                delay(300)
            }
            setState { copy(isLoading = false) }
        }
    }

    private fun loadCachedQuery() {
        val query = sharedPrefsProvider.loadQuery()
        setState { copy(query = query) }
    }

    private fun collectActionResult(actionResult: ActionResult) =
        setAction(WeatherMainAction.ShowSnackbar(actionResult))

    private fun collectWindSpeed(windSpeed: WindSpeed) = setState { copy(windSpeed = windSpeed) }

    private fun collectLanguage(language: Language) = setState { copy(lang = language) }

    private fun collectTemperature(temperature: Temperature) = setState {
        copy(temperature = temperature)
    }

    private fun collectPressure(pressure: Pressure) = setState {
        copy(pressure = pressure)
    }
}