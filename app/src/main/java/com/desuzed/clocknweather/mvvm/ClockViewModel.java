package com.desuzed.clocknweather.mvvm;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.desuzed.clocknweather.rx.HourObserver;
import com.desuzed.clocknweather.rx.MSecObserver;
import com.desuzed.clocknweather.rx.SecObserver;
import com.desuzed.clocknweather.rx.MinuteObserver;
import com.desuzed.clocknweather.util.MusicPlayer;
import com.desuzed.clocknweather.util.TimeGetter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;

public class ClockViewModel extends AndroidViewModel {
    private final Repository repo;
    private final Observable<Long> emitter;
    private final Predicate<Long> filterSeconds;

    public ClockViewModel(Application app) {
        super(app);
        repo = new Repository(app);
        emitter = Observable.interval(100, TimeUnit.MILLISECONDS);
        filterSeconds = aLong -> {
            // return aLong.toString().endsWith("0");
            return aLong % 10 == 0;
        };
    }
    public static final int ARROW_MIN = 10;
    public static final int ARROW_HOUR = 20;
    public void playMusic ( float rotation,int arrowIndex, MusicPlayer mp){
        switch (arrowIndex){
            case ARROW_HOUR:
                mp.playHourMusic();
                break;
            case ARROW_MIN:
                if (rotation == 90 || rotation == 180 || rotation == 270  || rotation == 0) {
                    mp.play15MinMusic();
                } else {
                    mp.playMinMusic();
                }
                MusicPlayer.ONLY_HOUR_MUSIC=false;
                break;
        }
    }

    private HourObserver hourObserver;
    private MinuteObserver minuteObserver;
    private SecObserver secObserver;
    private MSecObserver mSecObserver;

    public HourObserver getHourObserver() {
        if (hourObserver == null) {
            hourObserver = new HourObserver(getHourLiveData());
        }
        return hourObserver;
    }

    public MSecObserver getMSecObserver() {
        if (mSecObserver == null) {
            mSecObserver = new MSecObserver(getMSecLiveData());
        }
        return mSecObserver;
    }

    public MinuteObserver getMinuteObserver() {
        if (minuteObserver == null) {
            minuteObserver = new MinuteObserver(getMinLiveData());
        }
        return minuteObserver;
    }

    public SecObserver getSecObserver() {
        if (secObserver == null) {
            secObserver = new SecObserver(getSecLiveData());
        }
        return secObserver;
    }

    private MutableLiveData<Integer> hourLiveData;
    private MutableLiveData<Integer> minLiveData;
    private MutableLiveData<Integer> secLiveData;
    private MutableLiveData<Integer> mSecLiveData;

    public MutableLiveData<Integer> getHourLiveData() {
        if (hourLiveData == null) {
            hourLiveData = new MutableLiveData<>();
            hourLiveData.setValue(new TimeGetter().getHour());
        }
        return hourLiveData;
    }


    public MutableLiveData<Integer> getMinLiveData() {
        if (minLiveData == null) {
            minLiveData = new MutableLiveData<>();
            minLiveData.setValue(new TimeGetter().getMinute());
        }
        return minLiveData;
    }

    public MutableLiveData<Integer> getSecLiveData() {
        if (secLiveData == null) {
            secLiveData = new MutableLiveData<>();
            secLiveData.setValue(new TimeGetter().getSec());
        }
        return secLiveData;
    }
    public MutableLiveData<Integer> getMSecLiveData() {
        if (mSecLiveData == null) {
            mSecLiveData = new MutableLiveData<>();
            mSecLiveData.setValue(new TimeGetter().getMSec());
        }
        return mSecLiveData;
    }

    public Observable<Long> getEmitter() {
        return emitter;
    }

    public Predicate<Long> getFilterSeconds() {
        return filterSeconds;
    }

    public void setState(CheckBoxStates checkBoxStates) {
        repo.setState(checkBoxStates);
    }

    public LiveData<CheckBoxStates> getCheckBoxLiveData() {
        return repo.getCheckBoxLiveData();
    }


}
