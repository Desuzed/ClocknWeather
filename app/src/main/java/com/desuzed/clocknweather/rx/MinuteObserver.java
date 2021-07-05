package com.desuzed.clocknweather.rx;

import androidx.lifecycle.MutableLiveData;

import com.desuzed.clocknweather.util.TimeGetter;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class MinuteObserver implements Observer<Long> {
    private final MutableLiveData<Integer> minLiveData;

    public MinuteObserver(MutableLiveData<Integer> minLiveData) {
        this.minLiveData = minLiveData;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }
    public Integer getMinute (){
        return new TimeGetter().getMinute();
    }
    @Override
    public void onNext(@NonNull Long aLong) {
        Integer i = minLiveData.getValue();

        if (!Objects.equals(i, getMinute())) minLiveData.setValue(getMinute());
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }


}
