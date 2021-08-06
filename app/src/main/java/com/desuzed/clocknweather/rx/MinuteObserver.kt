package com.desuzed.clocknweather.rx

import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.util.TimeGetter
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MinuteObserver(private val minLiveData: MutableLiveData<Int>) : Observer<Long?> {
    override fun onSubscribe(d: @NonNull Disposable?) {}
//    val minute: Int
//        get() = TimeGetter().minute

    override fun onNext(aLong: @NonNull Long?) {
        val min = TimeGetter().minute
        val i = minLiveData.value
        if (i != min) {
            minLiveData.value = min
        }
    }

    override fun onError(e: @NonNull Throwable?) {}
    override fun onComplete() {}
}