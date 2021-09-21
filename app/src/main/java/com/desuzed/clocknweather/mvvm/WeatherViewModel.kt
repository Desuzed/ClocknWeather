package com.desuzed.clocknweather.mvvm

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desuzed.clocknweather.retrofit.WeatherApi
import com.desuzed.clocknweather.retrofit.pojo.Hour
import com.desuzed.clocknweather.retrofit.pojo.LatLon
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherViewModel(private val repo: Repository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    val weatherApiLiveData = MutableLiveData<WeatherApi>()
    val location = MutableLiveData(loadLastLocation())
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    fun getForecast(query: String) {
        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            val response = repo.getForecast(query)
                if (response.isSuccessful) {
                    weatherApiLiveData.postValue(response.body())
                    repo.saveForecast(response.body()!!, System.currentTimeMillis())
                } else {

                    onError("Error : ${response.message()} ")
                }
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun generateList(date: Long?, weatherApi: WeatherApi, timeZone : String): ArrayList<Hour> {
        val sdf = SimpleDateFormat("H")
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        val hour = sdf.format(date).toInt()
        val forecastDay = weatherApi.forecast?.forecastday
        val resultList: List<Hour> = forecastDay?.get(0)?.hour!!
            .drop(hour)
            .plus(forecastDay[1].hour!!.take(hour))
        return resultList as ArrayList<Hour>
    }

    fun getCachedForecast() {
        when (val result = repo.loadForecast()) {
            null -> {
                weatherApiLiveData.postValue(null)
            }
            else -> weatherApiLiveData.postValue(result)
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    private fun saveLastLocation(lastLocation: LatLon) {
        repo.saveLastLocation(lastLocation)
    }

    private fun loadLastLocation(): LatLon{
        return repo.loadLastLocation()
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        saveLastLocation(location.value!!)//TODO refactor
    }
}