package com.desuzed.everyweather.model.vm

import androidx.lifecycle.*
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.network.dto.weatherApi.ApiErrorMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseMapper
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.NetworkLiveData
import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.model.model.WeatherResponse
import com.desuzed.everyweather.view.StateUI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SharedViewModel (private val repo: RepositoryApp) : ViewModel() {
    val queryLiveData = MutableLiveData<String>()
    val location = MutableLiveData<LocationApp>()


    //Activity, LocationFragment, MapBotSheet
    fun postLocation(locationApp: LocationApp) {
        //  stateLiveData.postValue(StateUI.Loading())
        location.postValue(locationApp)
    }


//LocationFragment and MainFragment
    fun postQuery(query: String) {
   //     stateLiveData.postValue(StateUI.Loading())
        queryLiveData.postValue(query)
    }



//    private fun getCachedForecast(): WeatherResponse? {
//        stateLiveData.postValue(StateUI.Loading())
//        return when (val result = repo.loadForecast()) {
//            null -> {
//                stateLiveData.postValue(StateUI.NoData())
//                null
//            }
//            else -> {
//                stateLiveData.postValue(StateUI.Success())
//                result
//            }
//        }
//    }

//Main Fragment
    //TODO Избавиться
    fun loadCachedQuery() {
        val query = repo.loadQuery()
        if (query.isEmpty()) {
        //    stateLiveData.postValue(StateUI.NoData())
        }
        postQuery(query)
    }
//MainFragment
    fun getNetworkLiveData(): NetworkLiveData {
        return repo.getNetworkLiveData()
    }

}