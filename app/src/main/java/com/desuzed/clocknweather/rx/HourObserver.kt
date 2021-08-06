package com.desuzed.clocknweather.rx

import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.util.TimeGetter
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class HourObserver(private val hourLiveData: MutableLiveData<Int>) : Observer<Long?> {
    override fun onSubscribe(d: @NonNull Disposable?) {}
    override fun onNext(aLong: @NonNull Long?) {
        if (hourLiveData.value != hour) {
            hourLiveData.value = hour
        }
    }

    override fun onError(e: @NonNull Throwable?) {}
    override fun onComplete() {}
    private val hour: Int
        get() = TimeGetter().hour
}