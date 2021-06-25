package com.desuzed.clocknweather.mvvm;

public class ClockModel {
    private String textClock;
    private float hourRotation;
    private float minRotation;
    private float secRotation;

    public String getTextClock() {
        return textClock;
    }

    public void setTextClock(String textClock) {
        this.textClock = textClock;
    }

    public float getHourRotation() {
        return hourRotation;
    }

    public void setHourRotation(float hourRotation) {
        this.hourRotation = hourRotation;
    }

    public float getMinRotation() {
        return minRotation;
    }

    public void setMinRotation(float minRotation) {
        this.minRotation = minRotation;
    }

    public float getSecRotation() {
        return secRotation;
    }

    public void setSecRotation(float secRotation) {
        this.secRotation = secRotation;
    }
}
