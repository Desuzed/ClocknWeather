package com.desuzed.clocknweather.mvvm.vm

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desuzed.clocknweather.mvvm.NetworkLiveData
import com.desuzed.clocknweather.mvvm.repository.NetworkRepository
import com.desuzed.clocknweather.network.retrofit.NetworkResponse
import com.desuzed.clocknweather.network.dto.WeatherApi
import com.desuzed.clocknweather.network.dto.Hour
import com.desuzed.clocknweather.network.model.Query
import com.desuzed.clocknweather.ui.StateRequest
import kotlinx.coroutines.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class NetworkViewModel(private val repo: NetworkRepository) : ViewModel() {
    val stateLiveData = MutableLiveData<StateRequest>()
    val weatherApiLiveData = MutableLiveData<WeatherApi?>()
    val queryLiveData = MutableLiveData<Query>()

    //TODo REFACTOR
    fun getForecast(query: Query) = viewModelScope.launch(Dispatchers.IO) {
        stateLiveData.postValue(StateRequest.Loading())
        if (query.isEmpty()) {
            onError(repo.noDataToLoad())
            return@launch
        }
        when (val response = repo.getForecast(query)) {
            is NetworkResponse.Success -> {
                val body = response.body
                weatherApiLiveData.postValue(body)
                repo.saveForecast(body)
                if (query.userInput) {
                    val lat = body.locationDto?.lat
                    val lon = body.locationDto?.lon
                    val df = DecimalFormat("#.#")
                    df.roundingMode = RoundingMode.CEILING
                    val toStringLatLon = "${df.format(lat)},${df.format(lon)}"
                    stateLiveData.postValue(StateRequest.Success(toStringLatLon))
                } else {
                    stateLiveData.postValue(StateRequest.Success())

                }
            }
            is NetworkResponse.ApiError -> onError(repo.getApiError(response.body.error?.code))
            is NetworkResponse.NetworkError -> onError(repo.getInternetError())
            is NetworkResponse.UnknownError -> onError(repo.getUnknownError())
        }
    }


    fun postQuery(query: Query) {
        queryLiveData.postValue(query)
    }


//    fun postForecast(query: String) {
//        repo.saveQuery(query)
//        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
//            launchRefresh(true)
//            val response = repo.getForecast(query)
//            if (response.isSuccessful) {
//                weatherApiLiveData.postValue(response.body())
//                repo.saveForecast(response.body())
//                launchRefresh(false)
//            } else {
//                onError("Error : ${response.message()} ")
//                launchRefresh(false)
//            }
//        }
//    }


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

    //TODO doesnt stop refreshing when cant find location
    fun onError(message: String) {
        stateLiveData.postValue(StateRequest.Error(message))
    }


    fun loadCachedQuery() {
        val query = repo.loadQuery()
        if (query.isEmpty()) {
            stateLiveData.postValue(StateRequest.NoData())
        }
        queryLiveData.postValue(query)
    }

    fun getNetworkLiveData(): NetworkLiveData {
        return repo.getNetworkLiveData()
    }
}