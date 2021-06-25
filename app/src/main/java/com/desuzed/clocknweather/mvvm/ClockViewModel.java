package com.desuzed.clocknweather.mvvm;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.desuzed.clocknweather.R;
import com.desuzed.clocknweather.util.MusicPlayer;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;

public class ClockViewModel extends AndroidViewModel {
    private Repository repo;
    private final Observable<Long> emitter;
    private final Predicate<Long> filterSeconds;
    private CheckBoxStates mCheckBoxStates;
    private MutableLiveData <ClockModel> clockModelLiveData;

    public ClockViewModel(Application app) {
        super(app);
        repo = new Repository(app);
        emitter = Observable.interval(100, TimeUnit.MILLISECONDS);
        filterSeconds = aLong -> {
            // return aLong.toString().endsWith("0");
            return aLong % 10 == 0;
        };
    }

    public LiveData<ClockModel> getClockModelLiveData() {
        if (clockModelLiveData == null) {
            clockModelLiveData = new MutableLiveData<>();
        }
        return clockModelLiveData;
    }

    public Observable<Long> getEmitter() {
        return emitter;
    }

    public Predicate<Long> getFilterSeconds() {
        return filterSeconds;
    }

    public void setState (CheckBoxStates checkBoxStates){
        repo.setState(checkBoxStates);
    }

    public LiveData<CheckBoxStates> getCheckBoxLiveData() {
        return repo.getCheckBoxLiveData();
    }


//    @Override
//    public void rotationChanged(float rotation, int arrowIndex) {
//        switch (arrowIndex){
//            case ARROW_HOUR:
//                musicPlayer.playHourMusic();
//                break;
//            case ARROW_MIN:
//                if (rotation == 90 || rotation == 180 || rotation == 270  || rotation == 0) {
//                    musicPlayer.play15MinMusic();
//                } else {
//                    musicPlayer.playMinMusic();
//                }
//                MusicPlayer.ONLY_HOUR_MUSIC=false;
//                break;
//        }
//    }

//    public void playMinMusic() {
//        if (checkBoxManager.getCheckBoxStates().getStateMinute() && !ONLY_HOUR_MUSIC) {
//            MediaPlayer mpMin = MediaPlayer.create(context, R.raw.woody);
//            mpMin.start();
//            Log.i("MusicPlayer", "playMinMusic: ");
//        }
//    }
//
//    public void playHourMusic() {
//        if (checkBoxManager.getCheckBoxStates().getStateHour()) {
//            ONLY_HOUR_MUSIC = true;
//            MediaPlayer mpHour = MediaPlayer.create(context, R.raw.sound2);
//            mpHour.start();
//            Log.i("MusicPlayer", "playHOURMusic: ");
//        }
//
//    }
//
//    public void play15MinMusic() {
//        if (checkBoxManager.getCheckBoxStates().getState15min() && !ONLY_HOUR_MUSIC) {
//            MediaPlayer mp15min = MediaPlayer.create(context, R.raw.icq);
//            mp15min.start();
//            Log.i("MusicPlayer", "play15MinMusic: ");
//        }
//    }
}
