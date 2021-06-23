package com.desuzed.clocknweather.rx;

import android.widget.TextView;

import com.desuzed.clocknweather.util.ClockApp;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class BottomClockObserver implements Observer <Long> {
    private TextView tvBottomClock, tvTopClock;
    private ClockApp clock;

    public BottomClockObserver(TextView tvBottomClock, TextView tvTopClock, ClockApp clock) {
        this.tvBottomClock = tvBottomClock;
        this.tvTopClock = tvTopClock;
        this.clock = clock;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Long aLong) {
        tvBottomClock.setText(clock.setTimeBottomClock());
        tvTopClock.setText(clock.setTimeTopClock());
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
