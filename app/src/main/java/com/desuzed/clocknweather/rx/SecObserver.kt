package com.desuzed.clocknweather.rx

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.Disposable
import com.desuzed.clocknweather.util.TimeGetter
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observer
import java.util.*

class SecObserver(private val secLiveData: MutableLiveData<Int>) : Observer<Long?> {
    override fun onSubscribe(d: @NonNull Disposable?) {}
    override fun onNext(aLong: @NonNull Long?) {
        val sec = TimeGetter().sec
        secLiveData.value = sec
        //        if (!secLiveData.getValue().equals(sec)){
//            secLiveData.setValue(sec);
//        }
    }

    override fun onError(e: @NonNull Throwable?) {}
    override fun onComplete() {}
    val sec: Int
        get() = GregorianCalendar()[Calendar.SECOND]
}