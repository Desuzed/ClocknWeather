package com.desuzed.clocknweather.util;

import android.util.Log;

public class MusicPlayer {
    private CheckBoxManager checkBoxManager;
    public static final int MUSIC_MIN = 1;
    public static final int MUSIC_15_MIN = 2;
    public static final int MUSIC_HOUR = 3;

    public MusicPlayer(CheckBoxManager checkBoxManager) {
        this.checkBoxManager = checkBoxManager;
    }

    public void playMusic(int musicState, boolean onlyHourMusic){
        if (musicState==MUSIC_HOUR && checkBoxManager.getCheckBoxStates().getStateHour() && onlyHourMusic){
            Log.i("MusicPlayer", "playHOURMusic: ");
        }else if (musicState==MUSIC_15_MIN && checkBoxManager.getCheckBoxStates().getState15min() ){
            Log.i("MusicPlayer", "play15MinMusic: ");
        }else if (musicState==MUSIC_MIN && checkBoxManager.getCheckBoxStates().getStateMinute()){
            Log.i("MusicPlayer", "playMinMusic: ");
        }
//        if (checkBoxManager.getCheckBoxStates().getStateMinute()){
//
//        }
    }
}
