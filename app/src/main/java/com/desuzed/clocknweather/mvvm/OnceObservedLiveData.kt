package com.desuzed.clocknweather.mvvm

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class OnceObservedLiveData<T>: MutableLiveData<T>() {
    var isObserved = false
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
    }

    override fun postValue(value: T) {
        super.postValue(value)
        Log.i("onceObserved", ": false")
        isObserved=false
    }
}