package com.desuzed.everyweather.mvvm.vm

import androidx.lifecycle.*
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.NetworkLiveData
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.repository.RepositoryApp
import com.desuzed.everyweather.mvvm.room.RoomErrorHandler
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.network.retrofit.NetworkResponse
import com.desuzed.everyweather.ui.StateRequest
import com.desuzed.everyweather.util.mappers.ApiErrorMapper
import com.desuzed.everyweather.util.mappers.WeatherResponseMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SharedViewModel (private val repo: RepositoryApp) : ViewModel() {
    val stateLiveData = MutableLiveData<StateRequest>()
    val saveLocationVisibility = MutableLiveData <Boolean>(false)
    val weatherApiLiveData = MutableLiveData<WeatherResponse?>()
    val queryLiveData = MutableLiveData<String>()
    val location = MutableLiveData<LocationApp>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.getAllLocations()

    fun getForecast(query: String) = viewModelScope.launch(Dispatchers.IO) {
        stateLiveData.postValue(StateRequest.Loading())
        if (query.isEmpty()) {
            onError(repo.noDataToLoad())
            return@launch
        }
        when (val response = repo.getForecast(query)) {
            is NetworkResponse.Success -> {
                val weatherResponse = WeatherResponseMapper().mapFromEntity(response.body)
                weatherApiLiveData.postValue(weatherResponse)
                repo.saveForecast(weatherResponse)
                /**
                 * Triggers save location button on weather screen
                 **/
                if (isLocationSaved(weatherResponse)) {

                    stateLiveData.postValue(StateRequest.Success())
                } else {
                    stateLiveData.postValue(StateRequest.Success(true))
                }
            }
            is NetworkResponse.ApiError ->{
                val apiError = ApiErrorMapper().mapFromEntity(response.body)
                onError(repo.getApiError(apiError.error.code))
            }
            is NetworkResponse.NetworkError -> onError(repo.getInternetError())
            is NetworkResponse.UnknownError -> onError(repo.getUnknownError())
        }
    }


    fun postQuery(query: String) {
        stateLiveData.postValue(StateRequest.Loading())
        queryLiveData.postValue(query)
    }

    fun postLocation(locationApp: LocationApp) {
        stateLiveData.postValue(StateRequest.Loading())
        location.postValue(locationApp)
    }


    fun getCachedForecast() {
        stateLiveData.postValue(StateRequest.Loading())
        when (val result = repo.loadForecast()) {
            null -> {
                weatherApiLiveData.postValue(null)
                stateLiveData.postValue(StateRequest.NoData())
            }
            else -> {
                weatherApiLiveData.postValue(result)
                stateLiveData.postValue(StateRequest.Success())
            }

        }

    }

    fun onError(message: String) {
        stateLiveData.postValue(StateRequest.Error(message))
    }

    private fun onSuccess(message: String) {
        stateLiveData.postValue(StateRequest.Success(message))
    }

    fun loadCachedQuery() {
        val query = repo.loadQuery()
        if (query.isEmpty()) {
            stateLiveData.postValue(StateRequest.NoData())
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
        if (inserted.first == RoomErrorHandler.success) onSuccess(inserted.second)
        else if (inserted.first == RoomErrorHandler.fail) onError(inserted.second)
    }

    /**
     * Deletes item from DB. If success or not, user gets notification
     */
    fun deleteItem(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val deleted = repo.deleteItem(favoriteLocationDto)
        if (deleted.first == RoomErrorHandler.success) onSuccess(deleted.second)
        else if (deleted.first == RoomErrorHandler.fail) onError(deleted.second)
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
        saveLocationVisibility.postValue(state)
    }
}