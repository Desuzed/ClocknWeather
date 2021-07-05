package com.desuzed.clocknweather.rx;

import android.util.Log;

import com.desuzed.clocknweather.mvvm.ClockViewModel;
import com.desuzed.clocknweather.util.ClockApp;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class AnalogClockObserver implements Observer<Long> {
    private ClockApp clock;
    private ClockViewModel viewModel;

    public AnalogClockObserver(ClockApp clock) {
        this.clock = clock;
    }

    public AnalogClockObserver(ClockViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Long aLong) {
        clock.rotateAnalogClock();
 //       viewModel.rotateClock();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.i("AnalogClockObserver", "onError: " + e.getLocalizedMessage());
    }

    @Override
    public void onComplete() {

    }
}
