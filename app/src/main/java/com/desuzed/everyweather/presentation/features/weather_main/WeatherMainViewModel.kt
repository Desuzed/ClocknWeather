package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.analytics.WeatherMainAnalytics
import com.desuzed.everyweather.domain.interactor.LocationInteractor
import com.desuzed.everyweather.domain.interactor.WeatherInteractor
import com.desuzed.everyweather.domain.interactor.WeatherSettingsInteractor
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.model.settings.Language
import com.desuzed.everyweather.domain.model.settings.Pressure
import com.desuzed.everyweather.domain.model.settings.Temperature
import com.desuzed.everyweather.domain.model.settings.WindSpeed
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

class WeatherMainViewModel(
    private val weatherInteractor: WeatherInteractor,
    private val locationInteractor: LocationInteractor,
    private val analytics: WeatherMainAnalytics,
    private val weatherSettingsInteractor: WeatherSettingsInteractor,
    private val systemSettingsRepository: SystemSettingsRepository,
) : BaseViewModel<WeatherState, WeatherMainAction, WeatherUserInteraction>(WeatherState()) {

    private val queryResultFlow = MutableSharedFlow<QueryResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        getCachedForecast()
        loadCachedQuery()

        collect(weatherSettingsInteractor.distanceDimen, ::collectWindSpeed)
        collect(weatherSettingsInteractor.tempDimen, ::collectTemperature)
        collect(systemSettingsRepository.lang, ::collectLanguage)
        collect(weatherSettingsInteractor.pressureDimen, ::collectPressure)
        collect(queryResultFlow, ::collectActionResult)
        onUserInteraction(WeatherUserInteraction.Refresh)
    }

    fun getForecast(query: String, userLatLng: UserLatLng? = null) {
        launch {
            setState { copy(isLoading = true, query = query) }
            val fetchedForecast =
                weatherInteractor.fetchForecastOrErrorMessage(
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
            val isSaved = locationInteractor.saveLocationToDb(state.value.weatherData)
            if (isSaved) {
                onSuccess(ActionResultProvider.SAVED)
            } else {
                onError(ActionResultProvider.FAIL)
            }
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

    private suspend fun isLocationSaved(weatherContent: WeatherContent): Boolean {
        return locationInteractor.isLocationSaved(weatherContent.location)
    }

    private fun getCachedForecast() {
        launch {
            setState { copy(isLoading = true) }
            val result = weatherInteractor.getCachedForecast()
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
        val query = weatherInteractor.loadCachedQuery()
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