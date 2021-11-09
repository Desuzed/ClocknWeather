package com.desuzed.clocknweather.mvvm.vm

import androidx.lifecycle.*
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.repository.RepositoryApp
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import com.desuzed.clocknweather.ui.StateRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocationViewModel(private val repo: RepositoryApp) : ViewModel() {
    val location = MutableLiveData<LocationApp>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.getAllLocations()
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

    fun isSaved (latLon : String) : Boolean  = runBlocking {
             repo.containsPrimaryKey(latLon)
        }

}