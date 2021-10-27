package com.desuzed.clocknweather.mvvm.vm

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.mvvm.repository.NetworkRepository
import com.desuzed.clocknweather.network.WeatherApiService
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.dto.Hour
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class NetworkViewModel(private val repo: NetworkRepository) : ViewModel() {
    val queryLiveData = MutableLiveData<String>(loadQuery())
    private val errorMessage = MutableLiveData<String>()
    val refreshLiveData = MutableLiveData<Boolean>(false)
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val weatherApiLiveData = MutableLiveData<WeatherApi?>()

    //TODO handle correct errors
    fun postForecast(query: String) {
        repo.saveQuery(query)
        if (repo.getNetworkLiveData().value == false) {
            Log.i("TAG", "NO NETWORK, RETURNED")
            onError("NO NETWORK, RETURNED")
        }
        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            launchRefresh(true)
            val response = repo.getForecast(query)
            if (response.isSuccessful) {
                weatherApiLiveData.postValue(response.body())
                repo.saveForecast(response.body())
                launchRefresh(false)
            } else {
                onError("Error : ${response.message()} ")
                launchRefresh(false)
            }
        }
    }

    fun launchRefresh (state : Boolean){
        refreshLiveData.postValue(state)
    }


    //TODO rename method
    @SuppressLint("SimpleDateFormat")
    fun generateList(date: Long?, weatherApi: WeatherApi, timeZone: String): ArrayList<Hour> {
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
        errorMessage.postValue(message)
    }


    private fun loadQuery(): String? {
        return repo.loadQuery()
    }

    fun getNetworkLiveData(): NetworkLiveData {
        return repo.getNetworkLiveData()
    }

//    fun getWeatherLiveData(): MutableLiveData<WeatherApi> {
//        return repo.weatherApiLiveData
//    }



//    fun getQueryLiveData(): MutableLiveData<String> {
//        return repo.queryLiveData
//    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}