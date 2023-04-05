package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.analytics.WeatherMainAnalytics
import com.desuzed.everyweather.data.repository.local.SettingsDataStore
import com.desuzed.everyweather.data.repository.weather.WeatherRepository
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.repository.local.RoomProvider
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

class WeatherMainViewModel(
    private val weatherRepository: WeatherRepository,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val roomProvider: RoomProvider,
    private val analytics: WeatherMainAnalytics,
    private val settingsDataStore: SettingsDataStore,
) : BaseViewModel<WeatherState, WeatherMainAction, WeatherUserInteraction>(WeatherState()) {

    private val queryResultFlow = MutableSharedFlow<QueryResult>(
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
        collect(queryResultFlow, ::collectActionResult)
        onUserInteraction(WeatherUserInteraction.Refresh)
    }

    fun getForecast(query: String, userLatLng: UserLatLng? = null) {
        launch {
            setState { copy(isLoading = true, query = query) }
            val fetchedForecast =
                weatherRepository.fetchForecastOrErrorMessage(
                    query = query,
                    lang = state.value.lang.id.lowercase(),
                    userLatLng = userLatLng,
                )
            val weatherResponse = fetchedForecast.weatherContent
            val actionResult = fetchedForecast.queryResult
            val isLocationSaved = weatherResponse?.let { isLocationSaved(it) }
            setState {
                copy(
                    weatherData = weatherResponse,
                    isLoading = false,
                    isAddButtonEnabled = isLocationSaved?.not() ?: false,
                )
            }
            if (actionResult != null) {
                queryResultFlow.emit(actionResult)
                setState {
                    copy(
                        isLoading = false,
                        isAddButtonEnabled = false,
                    )
                }
            }
        }
    }

    override fun onUserInteraction(interaction: WeatherUserInteraction) {
        analytics.onUserInteraction(interaction)
        when (interaction) {
            WeatherUserInteraction.Location -> setAction(WeatherMainAction.NavigateToLocation)
            WeatherUserInteraction.NextDays -> setAction(WeatherMainAction.NavigateToNextDaysWeather)
            WeatherUserInteraction.Refresh -> getForecast(state.value.query)
            WeatherUserInteraction.SaveLocation -> saveLocation()
            WeatherUserInteraction.Redirection -> launch {
                queryResultFlow.emit(QueryResult(ActionResultProvider.REDIRECTION))
            }
        }
    }

    private fun saveLocation() {
        launch {
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
        launch {
            queryResultFlow.emit(QueryResult(code = code))
        }
    }

    private fun onSuccess(code: Int) {
        launch {
            queryResultFlow.emit(QueryResult(code = code))
            setState {
                copy(isAddButtonEnabled = false)
            }
        }

    }

    private suspend fun isLocationSaved(response: WeatherContent): Boolean {
        val latLonKey = FavoriteLocationDto.generateKey(response.location)
        return roomProvider.containsPrimaryKey(latLonKey)
    }

    private fun getCachedForecast() {
        launch {
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

    private fun collectActionResult(queryResult: QueryResult) =
        setAction(WeatherMainAction.ShowSnackbar(queryResult))

    private fun collectWindSpeed(windSpeed: WindSpeed) = setState { copy(windSpeed = windSpeed) }

    private fun collectLanguage(language: Language) = setState { copy(lang = language) }

    private fun collectTemperature(temperature: Temperature) = setState {
        copy(temperature = temperature)
    }

    private fun collectPressure(pressure: Pressure) = setState {
        copy(pressure = pressure)
    }
}