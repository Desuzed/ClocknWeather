package com.desuzed.clocknweather.rx;

import androidx.lifecycle.MutableLiveData;

import com.desuzed.clocknweather.util.TimeGetter;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class MSecObserver implements Observer <Long> {
    private final MutableLiveData<Integer> mSecLiveData;

    public MSecObserver(MutableLiveData<Integer> mSecLiveData) {
        this.mSecLiveData = mSecLiveData;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Long aLong) {
//        Log.i("TAG", "onNext: milsec");
//        Integer mSec = new TimeGetter().getMSec();
//        if (!mSecLiveData.getValue().equals(mSec)){
//            mSecLiveData.setValue(mSec);
//        }
        mSecLiveData.setValue(new TimeGetter().getMSec());
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
