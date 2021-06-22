package com.desuzed.clocknweather.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.desuzed.clocknweather.R;

public class MusicPlayer {
    private final CheckBoxManager checkBoxManager;
    public static boolean ONLY_HOUR_MUSIC = false;
    private final Context context;

    public MusicPlayer(CheckBoxManager checkBoxManager, Context context) {
        this.checkBoxManager = checkBoxManager;
        this.context = context;
    }

    public void playMinMusic() {
        if (checkBoxManager.getCheckBoxStates().getStateMinute() && !ONLY_HOUR_MUSIC) {
            MediaPlayer mpMin = MediaPlayer.create(context, R.raw.woody);
            mpMin.start();
            Log.i("MusicPlayer", "playMinMusic: ");
        }
    }

    public void playHourMusic() {
        if (checkBoxManager.getCheckBoxStates().getStateHour()) {
            ONLY_HOUR_MUSIC = true;
            MediaPlayer mpHour = MediaPlayer.create(context, R.raw.sound2);
            mpHour.start();
            Log.i("MusicPlayer", "playHOURMusic: ");
        }

    }

    public void play15MinMusic() {
        if (checkBoxManager.getCheckBoxStates().getState15min() && !ONLY_HOUR_MUSIC) {
            MediaPlayer mp15min = MediaPlayer.create(context, R.raw.icq);
            mp15min.start();
            Log.i("MusicPlayer", "play15MinMusic: ");
        }
    }
}
