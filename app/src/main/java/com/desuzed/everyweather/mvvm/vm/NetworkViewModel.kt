package com.desuzed.everyweather.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desuzed.everyweather.mvvm.repository.RepositoryApp
import com.desuzed.everyweather.ui.StateRequest

class NetworkViewModel(private val repo: RepositoryApp) : ViewModel() {
    val stateLiveData = MutableLiveData<StateRequest>()




}