package com.desuzed.clocknweather.mvvm.vm

import androidx.lifecycle.*
import com.desuzed.clocknweather.mvvm.LocationApp
import com.desuzed.clocknweather.mvvm.repository.FavoriteLocationRepository
import com.desuzed.clocknweather.mvvm.room.model.FavoriteLocationDto
import kotlinx.coroutines.launch

class LocationViewModel(private val repo: FavoriteLocationRepository) : ViewModel() {
    val saveLocationVisibility = MutableLiveData <Boolean>(false)
    val location = MutableLiveData<LocationApp>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.allLocations.asLiveData()

    fun insert(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        repo.insert(favoriteLocationDto)
    }

    fun postLocationVisibility (state : Boolean){
        saveLocationVisibility.postValue(state)
    }


    fun deleteItem(favoriteLocationDto: FavoriteLocationDto) = viewModelScope.launch {
        repo.deleteItem(favoriteLocationDto)
    }


}