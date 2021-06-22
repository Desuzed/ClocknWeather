package com.desuzed.clocknweather.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Repository {
    public static final String STATE_MIN = "STATE_MIN";
    public static final String STATE_15_MIN = "STATE_15_MIN";
    public static final String STATE_HOUR = "STATE_HOUR";
    public static final String S_PREF_NAME = "SP";
    private MutableLiveData<CheckBoxStates> checkBoxLiveData;
    private final Application app;


    public Repository(Application app) {
        this.app = app;
    }

    public LiveData<CheckBoxStates> getCheckBoxLiveData() {
        if (checkBoxLiveData == null) {
            checkBoxLiveData = new MutableLiveData<>();
            boolean stateMin = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE).getBoolean(STATE_MIN, false);
            boolean state15Min = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE).getBoolean(STATE_15_MIN, false);
            boolean stateHour = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE).getBoolean(STATE_HOUR, false);
            checkBoxLiveData.setValue(new CheckBoxStates(stateMin, state15Min, stateHour));
        }
        return checkBoxLiveData;
    }

    public void setState(CheckBoxStates checkBoxStates) {
        checkBoxLiveData.setValue(checkBoxStates);
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(STATE_MIN, checkBoxStates.getStateMinute())
                .putBoolean(STATE_15_MIN, checkBoxStates.getState15min())
                .putBoolean(STATE_HOUR, checkBoxStates.getStateHour())
                .apply();
    }
}
