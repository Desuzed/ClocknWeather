package com.desuzed.clocknweather.mvvm.vm

import androidx.lifecycle.*
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.repository.FavoriteLocationRepository
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.ui.StateRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocationViewModel(private val repo: FavoriteLocationRepository) : ViewModel() {
    val location = MutableLiveData<LocationApp>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.allLocations.asLiveData()
    val stateLiveData = MutableLiveData<StateRequest>()


    fun insert(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        repo.insert(favoriteLocationDto)
    }

    fun deleteItem(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        repo.deleteItem(favoriteLocationDto)
    }

    fun onError(message: String) {
        stateLiveData.postValue(StateRequest.Error(message))
    }

//TODO проверять есть ли элемент по ключу или нет

    fun isSaved (latLon : String) : Boolean  = runBlocking {
             repo.containsPrimaryKey(latLon)
        }

}