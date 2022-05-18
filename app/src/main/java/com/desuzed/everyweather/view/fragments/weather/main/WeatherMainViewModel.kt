package com.desuzed.everyweather.view.fragments.weather.main

import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.BaseViewModel
import kotlinx.coroutines.launch


class WeatherMainViewModel(private val repo: RepositoryApp) :
    BaseViewModel<WeatherState, WeatherMainAction>(
        WeatherState()
    ) {

    init {
        getCachedForecast()
        loadCachedQuery()
    }

    fun getForecast(query: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, query = query) }
            val fetchedForecast = repo.fetchForecastOrErrorMessage(query)
            val weatherResponse = fetchedForecast.getWeatherResponse()
            val message = fetchedForecast.getMessage()
            if (weatherResponse != null) {
                val isLocationSaved = isLocationSaved(weatherResponse)
                setState {
                    copy(
                        weatherData = weatherResponse,
                        isLoading = false,
                        isAddButtonEnabled = isLocationSaved,
                    )
                }
                return@launch
            } else if (message != null) {
                setState {
                    copy(
                        infoMessage = message,
                        isLoading = false,
                        isAddButtonEnabled = false,
                    )
                }
            }
        }
    }

    fun insert(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val inserted = repo.insert(favoriteLocationDto)
        if (inserted) onSuccess(ActionResultProvider.SAVED)
        else onError(ActionResultProvider.FAIL)
    }

    fun toggleSaveButton(state: Boolean) {
        setState { copy(isAddButtonEnabled = state) }
    }

    private fun onError(code: Int) {
        val message = repo.parseCode(code)
        setState { copy(infoMessage = message) }
    }

    private fun onSuccess(code: Int) {
        val message = repo.parseCode(code)
        setState { copy(infoMessage = message) }
    }

    private suspend fun isLocationSaved(response: WeatherResponse): Boolean {
        val latLonKey = FavoriteLocationDto.generateKey(response.location)
        return repo.containsPrimaryKey(latLonKey)
    }

    private fun getCachedForecast() {
        setState { copy(isLoading = true) }
        val result = repo.loadForecastFromCache()
        setState { copy(weatherData = result, isLoading = false) }
    }

    private fun loadCachedQuery() {
        val query = repo.loadQuery()
        setState { copy(query = query) }
    }

}