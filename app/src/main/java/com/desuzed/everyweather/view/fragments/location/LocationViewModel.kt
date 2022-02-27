package com.desuzed.everyweather.view.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.network.ActionResultProvider
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.model.Event
import kotlinx.coroutines.launch

class LocationViewModel(private val repo: RepositoryApp) : ViewModel() {
    val messageLiveData = MutableLiveData<Event<String>>()
    val allLocations: LiveData<List<FavoriteLocationDto>> = repo.getAllLocations()

    fun onError(code: Int) {
        val message = repo.parseCode(code)
        messageLiveData.postValue(Event(message))
    }

    private fun onSuccess(code: Int) {
        val message = repo.parseCode(code)
        messageLiveData.postValue(Event(message))
    }

    fun loadCachedLocation () = repo.loadForecastFromCache()?.location

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