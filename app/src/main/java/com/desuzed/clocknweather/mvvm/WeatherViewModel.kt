package com.desuzed.clocknweather.mvvm

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desuzed.clocknweather.retrofit.OnecallApi
import kotlinx.coroutines.*

class WeatherViewModel(private val repo: Repository) : ViewModel() {
    val weatherLiveData = MutableLiveData<OnecallApi>()
    val errorMessage = MutableLiveData<String>()
    val loadMessage = MutableLiveData<String>()
    val location = MutableLiveData <Location>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getOnecallForecast(location: Location) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repo.getForecastOnecall(location.latitude, location.longitude)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful){
                    weatherLiveData.postValue(response.body())
                    repo.saveForecast(response.body()!!, System.currentTimeMillis())
                }else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun getCachedForecast (){
        val result = repo.loadForecast()
        loadMessage.postValue(result.first)
        when {
            result.first.equals(Repository.NO_VALUE) -> {
                weatherLiveData.postValue(null)
            }
            else -> weatherLiveData.postValue(result.second)
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