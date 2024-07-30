package com.desuzed.everyweather.presentation.features.weather_main

import com.desuzed.everyweather.analytics.WeatherMainAnalytics
import com.desuzed.everyweather.domain.interactor.LocationInteractor
import com.desuzed.everyweather.domain.interactor.WeatherInteractor
import com.desuzed.everyweather.domain.interactor.WeatherSettingsInteractor
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.model.settings.DistanceDimen
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.settings.PressureDimen
import com.desuzed.everyweather.domain.model.settings.TempDimen
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import com.desuzed.everyweather.presentation.base.BaseViewModel
import kotlinx.coroutines.delay

class WeatherMainViewModel(
    private val weatherInteractor: WeatherInteractor,
    private val locationInteractor: LocationInteractor,
    private val analytics: WeatherMainAnalytics,
    private val weatherSettingsInteractor: WeatherSettingsInteractor,
    private val systemSettingsRepository: SystemSettingsRepository,
) : BaseViewModel<WeatherState, WeatherMainEffect, WeatherAction>(WeatherState()) {

    init {
        getCachedForecast()
        loadCachedQuery()

        collect(weatherSettingsInteractor.distanceDimen, ::collectWindSpeed)
        collect(weatherSettingsInteractor.tempDimen, ::collectTemperature)
        collect(systemSettingsRepository.lang, ::collectLanguage)
        collect(weatherSettingsInteractor.pressureDimen, ::collectPressure)
        onAction(WeatherAction.Refresh)
    }

    fun getForecast(query: String, userLatLng: UserLatLng? = null) {
        launch {
            setState { copy(isLoading = true, query = query) }
            val fetchedForecast =
                weatherInteractor.fetchForecastOrErrorMessage(
                    query = query,
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
                setSideEffect(WeatherMainEffect.ShowSnackbar(actionResult))
                setState {
                    copy(
                        isLoading = false,
                        isAddButtonEnabled = false,
                    )
                }
            }
        }
    }

    override fun onAction(action: WeatherAction) {
        analytics.onAction(action)
        when (action) {
            WeatherAction.Location -> setSideEffect(WeatherMainEffect.NavigateToLocation)
            WeatherAction.Refresh -> getForecast(state.value.query)
            WeatherAction.SaveLocation -> saveLocation()
            WeatherAction.Redirection -> onRedirection()
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
        setSideEffect(WeatherMainEffect.ShowSnackbar(QueryResult(code = code)))
    }

    private fun onSuccess(code: Int) {
        setSideEffect(WeatherMainEffect.ShowSnackbar(QueryResult(code = code)))
        setState {
            copy(isAddButtonEnabled = false)
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

    private fun onRedirection() {
        setSideEffect(
            WeatherMainEffect.ShowSnackbar(
                queryResult = QueryResult(ActionResultProvider.REDIRECTION),
            )
        )
    }

    private fun collectWindSpeed(windSpeed: DistanceDimen) =
        setState { copy(windSpeed = windSpeed) }

    private fun collectLanguage(language: Lang) = setState { copy(selectedLang = language) }

    private fun collectTemperature(temperature: TempDimen) = setState {
        copy(temperature = temperature)
    }

    private fun collectPressure(pressure: PressureDimen) = setState {
        copy(pressure = pressure)
    }
}