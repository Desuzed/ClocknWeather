package com.desuzed.clocknweather.rx;

import androidx.lifecycle.MutableLiveData;

import com.desuzed.clocknweather.util.TimeGetter;

import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class HourObserver implements Observer <Long> {
    private final MutableLiveData<Integer> hourLiveData;

    public HourObserver(MutableLiveData<Integer> hourLiveData) {
        this.hourLiveData = hourLiveData;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Long aLong) {
        if (!Objects.equals(hourLiveData.getValue(), getHour())){
            hourLiveData.setValue(getHour());
        }

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public Integer getHour (){
        return new TimeGetter().getHour();
    }
}
