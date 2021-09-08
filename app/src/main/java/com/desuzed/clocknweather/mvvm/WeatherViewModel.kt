package com.desuzed.clocknweather.mvvm

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desuzed.clocknweather.retrofit.FiveDayForecast
import com.desuzed.clocknweather.retrofit.OnecallApi
import kotlinx.coroutines.*

class WeatherViewModel(private val repo: Repository) : ViewModel() {
    val oneCallLiveData = MutableLiveData<OnecallApi>()
    val fiveDayForecastLiveData = MutableLiveData<FiveDayForecast>()
    val errorMessage = MutableLiveData<String>()
    val loadMessage = MutableLiveData<String>()
    val location = MutableLiveData<Location>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

     fun getFiveDayForecast(city: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repo.getFiveDayForecast(city)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                   fiveDayForecastLiveData.postValue(response.body())
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }


    fun getOnecallForecast(location: Location) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repo.getForecastOnecall(location.latitude, location.longitude)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    oneCallLiveData.postValue(response.body())
                    repo.saveForecast(response.body()!!, System.currentTimeMillis())
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun getCachedForecast() {
        val result = repo.loadForecast()
        loadMessage.postValue(result.first)
        when {
            result.first == Repository.NO_VALUE -> {
                oneCallLiveData.postValue(null)
            }
            else -> oneCallLiveData.postValue(result.second)
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}