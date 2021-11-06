package com.desuzed.clocknweather.mvvm.vm

import androidx.lifecycle.*
import com.desuzed.clocknweather.ui.StateRequest

class SharedViewModel() : ViewModel() {
    //TODO Мб здесь будет роутер
    val stateLiveData = MutableLiveData<StateRequest>()
    val saveLocationVisibility = MutableLiveData <Boolean>(false)


    fun toggleSaveButton (state : Boolean){
        saveLocationVisibility.postValue(state)
    }
}