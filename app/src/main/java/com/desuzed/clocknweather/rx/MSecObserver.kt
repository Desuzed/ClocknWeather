package com.desuzed.clocknweather.rx

import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.util.TimeGetter
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MSecObserver(private val mSecLiveData: MutableLiveData<Int>) : Observer<Long?> {
    override fun onSubscribe(d: @NonNull Disposable?) {}
    override fun onNext(aLong: @NonNull Long?) {
//        Log.i("TAG", "onNext: milsec");
//        Integer mSec = new TimeGetter().getMSec();
//        if (!mSecLiveData.getValue().equals(mSec)){
//            mSecLiveData.setValue(mSec);
//        }
        mSecLiveData.value = TimeGetter().mSec
    }

    override fun onError(e: @NonNull Throwable?) {}
    override fun onComplete() {}
}