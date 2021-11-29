package com.desuzed.everyweather.mvvm.vm

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.NetworkLiveData
import com.desuzed.everyweather.mvvm.model.Hour
import com.desuzed.everyweather.mvvm.model.WeatherResponse
import com.desuzed.everyweather.mvvm.repository.RepositoryApp
import com.desuzed.everyweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.everyweather.network.retrofit.NetworkResponse
import com.desuzed.everyweather.ui.StateRequest
import com.desuzed.everyweather.util.mappers.ApiErrorMapper
import com.desuzed.everyweather.util.mappers.WeatherResponseMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class SharedViewModel (private val repo: RepositoryApp) : ViewModel() {
    //TODO Мб здесь будет роутер
    val stateLiveData = MutableLiveData<StateRequest>()
    val saveLocationVisibility = MutableLiveData <Boolean>(false)
    val weatherApiLiveData = MutableLiveData<WeatherResponse?>()
    val queryLiveData = MutableLiveData<String>()
    val location = MutableLiveData<LocationApp>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.getAllLocations()

    //TODo REFACTOR
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
                if (isLocationSaved(weatherResponse)){
                    //Triggers save location button
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

    //TODO refactor to mapper
    @SuppressLint("SimpleDateFormat")
    fun generateCurrentDayList(date: Long, weatherResponse: WeatherResponse, timeZone: String): ArrayList<Hour> {
        val sdf = SimpleDateFormat("H")
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        val hour = sdf.format(date).toInt()
        val forecastDay = weatherResponse.forecastDay
        val currentDayList: List<Hour> = forecastDay.get(0).hourForecast
            .drop(hour)
            .plus(forecastDay[1].hourForecast.take(hour))
        return currentDayList as ArrayList<Hour>
    }

    fun getCachedForecast() {
        stateLiveData.postValue(StateRequest.Loading())
        when (val result = repo.loadForecast()) {
            null -> {
                weatherApiLiveData.postValue(null)
                stateLiveData.postValue(StateRequest.NoData()) //todo нужен ли этот стейт вообще
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



    fun insert(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        repo.insert(favoriteLocationDto)
    }

    fun deleteItem(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        repo.deleteItem(favoriteLocationDto)
    }

    /**
     * Method checks if database contains place with that coordinates
     */
    fun isLocationSaved (response:  WeatherResponse) : Boolean  = runBlocking {
        //todo refactor
        val lat = response.location.lat
        val lon = response.location.lon
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        val latLonKey = "${df.format(lat)},${df.format(lon)}"
        // Returns boolean
        repo.containsPrimaryKey(latLonKey)
    }


    fun toggleSaveButton (state : Boolean){
        saveLocationVisibility.postValue(state)
    }
}