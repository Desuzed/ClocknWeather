package com.desuzed.everyweather.view

import androidx.lifecycle.*
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.NetworkLiveData
import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.model.model.WeatherResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SharedViewModel (private val repo: RepositoryApp) : ViewModel() {
    val stateLiveData = MutableLiveData<StateUI>()
    val toggleLocationVisibility = MutableLiveData<Boolean>(false)
    val weatherApiLiveData = MutableLiveData<WeatherResponse?>(getCachedForecast())
    val queryLiveData = MutableLiveData<String>()
    val location = MutableLiveData<LocationApp>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.getAllLocations()

    fun getForecast(query: String) = viewModelScope.launch {
        stateLiveData.postValue(StateUI.Loading())
        val fetchedForecast = repo.fetchForecastOrErrorMessage(query)
        val weatherResponse = fetchedForecast.getWeatherResponse()
        val errorMessage = fetchedForecast.getErrorMessage()
        if (weatherResponse != null) {
            weatherApiLiveData.postValue(fetchedForecast.getWeatherResponse())
            if (isLocationSaved(weatherResponse)) {
                stateLiveData.postValue(StateUI.Success())
            } else {
                stateLiveData.postValue(StateUI.Success(true))
            }
            return@launch
        } else if (errorMessage != null) {
            stateLiveData.postValue(StateUI.Error(errorMessage))
        }
    }


    fun postQuery(query: String) {
        stateLiveData.postValue(StateUI.Loading())
        queryLiveData.postValue(query)
    }

    fun postLocation(locationApp: LocationApp) {
        stateLiveData.postValue(StateUI.Loading())
        location.postValue(locationApp)
    }


    private fun getCachedForecast(): WeatherResponse? {
        stateLiveData.postValue(StateUI.Loading())
        return when (val result = repo.loadForecastFromCache()) {
            null -> {
                stateLiveData.postValue(StateUI.NoData())
                null
            }
            else -> {
                stateLiveData.postValue(StateUI.Success())
                result
            }
        }
    }

    fun onError(code: Int) {
        val message = repo.parseCode(code)
        stateLiveData.postValue(StateUI.Error(message))
    }

    private fun onSuccess(code: Int) {
        val message = repo.parseCode(code)
        stateLiveData.postValue(StateUI.Success(message))
    }

    //TODO Избавиться
    fun loadCachedQuery() {
        val query = repo.loadQuery()
        if (query.isEmpty()) {
            stateLiveData.postValue(StateUI.NoData())
        }
        postQuery(query)
    }

    fun getNetworkLiveData(): NetworkLiveData {
        return repo.getNetworkLiveData()
    }

    /**
     * Inserts item to DB. If success or not, user gets notification
     */
    fun insert(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val inserted = repo.insert(favoriteLocationDto)
        if (inserted) onSuccess(ActionResultProvider.INSERTED)
        else onError(ActionResultProvider.FAIL)
    }

    /**
     * Deletes item from DB. If success or not, user gets notification
     */
    fun deleteItem(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val deleted = repo.deleteItem(favoriteLocationDto)
        if (deleted) onSuccess(ActionResultProvider.DELETED)
        else onError(ActionResultProvider.FAIL)
    }

    /**
     * Method checks if database contains place with that coordinates
     */
    private fun isLocationSaved(response: WeatherResponse): Boolean = runBlocking {
        val latLonKey = FavoriteLocationDto.generateKey(response.location)
        // Returns boolean
        repo.containsPrimaryKey(latLonKey)
    }


    fun toggleSaveButton (state : Boolean){
        toggleLocationVisibility.postValue(state)
    }
}