package com.desuzed.everyweather.model.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.network.dto.weatherApi.ApiErrorMapper
import com.desuzed.everyweather.data.network.dto.weatherApi.WeatherResponseMapper
import com.desuzed.everyweather.data.network.retrofit.NetworkResponse
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.model.LocationApp
import com.desuzed.everyweather.model.model.WeatherResponse
import com.desuzed.everyweather.view.StateUI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocationViewModel (private val repo: RepositoryApp) : ViewModel()  {
    val messageLiveData  = MutableLiveData <String>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.getAllLocations()

    fun onError(code: Int) {
        val message = repo.parseCode(code)
        messageLiveData.postValue(message)
    }
    private fun onSuccess(code: Int) {
        val message = repo.parseCode(code)
        messageLiveData.postValue(message)
    }
    /**
     * Deletes item from DB. If success or not, user gets notification
     */
    //LocationFragment
    fun deleteItem(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        val deleted = repo.deleteItem(favoriteLocationDto)
        if (deleted) onSuccess(ActionResultProvider.DELETED)
        else onError(ActionResultProvider.FAIL)
    }
}