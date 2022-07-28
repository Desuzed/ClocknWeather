package com.desuzed.everyweather.presentation.features.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desuzed.everyweather.data.repository.RepositoryApp
import com.desuzed.everyweather.domain.model.UserLatLng
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repo: RepositoryApp) : ViewModel() {

    private val _isLookingForLocation = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val isLookingForLocation: Flow<Boolean> = _isLookingForLocation.asSharedFlow()

    val hasInternet = repo.getNetworkConnection()

    val userLatLng = MutableStateFlow<UserLatLng?>(null)

    private val _messageFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messageFlow: Flow<String> = _messageFlow.asSharedFlow()

    fun postMessage(message: String){
        viewModelScope.launch {
            _messageFlow.emit(message)
        }
    }

    fun toggleLookingForLocation(state: Boolean) {
        viewModelScope.launch {
            _isLookingForLocation.emit(state)
        }
    }

}