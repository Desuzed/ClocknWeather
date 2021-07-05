package com.desuzed.clocknweather.rx;

import androidx.lifecycle.MutableLiveData;

import com.desuzed.clocknweather.util.TimeGetter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SecObserver implements Observer<Long>  {
    private final MutableLiveData<Integer> secLiveData;

    public SecObserver(MutableLiveData<Integer> mSecLiveData) {
        this.secLiveData = mSecLiveData;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Long aLong) {
        Integer sec = new TimeGetter().getSec();
        secLiveData.setValue(sec);
//        if (!secLiveData.getValue().equals(sec)){
//            secLiveData.setValue(sec);
//        }
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public Integer getSec(){
        return new GregorianCalendar().get(Calendar.SECOND);
    }
}
