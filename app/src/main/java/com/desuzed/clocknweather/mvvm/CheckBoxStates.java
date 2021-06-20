package com.desuzed.clocknweather.mvvm;

public class CheckBoxStates {
    private boolean stateMinute;
    private boolean state15min;
    private boolean stateHour;

    public CheckBoxStates() {
    }

    public CheckBoxStates(boolean stateMinute, boolean state15min, boolean stateHour) {
        this.stateMinute = stateMinute;
        this.state15min = state15min;
        this.stateHour = stateHour;
    }

    public void setStates (boolean stateMin, boolean state15Min, boolean stateHour){
        this.stateMinute = stateMin;
        this.state15min = state15Min;
        this.stateHour = stateHour;
    }

    public boolean getStateHour() {
        return stateHour;
    }

    public void setStateHour(boolean stateHour) {
        this.stateHour = stateHour;
    }

    public boolean getState15min() {
        return state15min;
    }

    public void setState15min(boolean state15min) {
        this.state15min = state15min;
    }

    public boolean getStateMinute() {
        return stateMinute;
    }

    public void setStateMinute(boolean stateMinute) {
        this.stateMinute = stateMinute;
    }

    public static interface OnCheckBoxStateChanged {
        void onStateChanged ();
    }
}
