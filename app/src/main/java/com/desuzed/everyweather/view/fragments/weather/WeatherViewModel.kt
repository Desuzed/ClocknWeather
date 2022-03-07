package com.desuzed.everyweather.view.fragments.weather


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.Event
import com.desuzed.everyweather.model.entity.WeatherResponse
import com.desuzed.everyweather.view.StateUI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WeatherViewModel(private val repo: RepositoryApp) : ViewModel() {
    val stateLiveData = MutableLiveData<Event<StateUI>>()
    val weatherApiLiveData = MutableLiveData<WeatherResponse?>()
    val toggleLocationVisibility = MutableLiveData<Boolean>(false)
    val queryLiveData = MutableLiveData<String>()

    //todo _...LiveData
    init {
        weatherApiLiveData.value = getCachedForecast()
        loadCachedQuery()
        queryLiveData.value?.let { getForecast(it) }
    }

    fun getForecast(query: String) {
        viewModelScope.launch {
            stateLiveData.postValue(Event(StateUI.Loading()))
            queryLiveData.postValue(query)
            val fetchedForecast = repo.fetchForecastOrErrorMessage(query)
            val weatherResponse = fetchedForecast.getWeatherResponse()
            val message = fetchedForecast.getMessage()
            if (weatherResponse != null) {
                weatherApiLiveData.postValue(weatherResponse)
                if (isLocationSaved(weatherResponse)) {
                    stateLiveData.postValue(Event(StateUI.Success(message)))
                } else {
                    stateLiveData.postValue(Event(StateUI.Success(message, true)))
                }
                return@launch
            } else if (message != null) {
                stateLiveData.postValue(Event(StateUI.Error(message)))
            }
        }
    }

    private fun getCachedForecast(): WeatherResponse? {
        stateLiveData.postValue(Event(StateUI.Loading()))
        return when (val result = repo.loadForecastFromCache()) {
            null -> {
                stateLiveData.postValue(Event(StateUI.NoData()))
                null
            }
            else -> {
                stateLiveData.postValue(Event(StateUI.Success()))
                result
            }
        }
    }

    private fun onError(code: Int) {
        val message = repo.parseCode(code)
        stateLiveData.postValue(Event(StateUI.Error(message)))
    }

    private fun onSuccess(code: Int) {
        val message = repo.parseCode(code)
        stateLiveData.postValue(Event(StateUI.Success(message)))
    }

    private fun loadCachedQuery() {
        val query = repo.loadQuery()
        if (query.isEmpty()) {
            stateLiveData.postValue(Event(StateUI.NoData()))
        }
        queryLiveData.value = query
    }

    /**
     * Method checks if database contains place with that coordinates
     */
    private fun isLocationSaved(response: WeatherResponse): Boolean = runBlocking {
        val latLonKey = FavoriteLocationDto.generateKey(response.location)
        repo.containsPrimaryKey(latLonKey)
    }

    fun toggleSaveButton(state: Boolean) {
        toggleLocationVisibility.postValue(state)
    }


    /**
     * Inserts item to DB. If success or not, user gets notification
     */
    fun insert(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val inserted = repo.insert(favoriteLocationDto)
        if (inserted) onSuccess(ActionResultProvider.SAVED)
        else onError(ActionResultProvider.FAIL)
    }

}