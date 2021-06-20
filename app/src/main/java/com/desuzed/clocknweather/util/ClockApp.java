package com.desuzed.clocknweather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClockApp implements ArrowImageView.OnRotationChangedListener {
    public static final int ARROW_MIN = 10;
    public static final int ARROW_HOUR = 20;
    private ArrowImageView arrowSeconds, arrowMinutes, arrowHours;
    private MusicPlayer musicPlayer;
    private SimpleDateFormat sdfBottomClock = new SimpleDateFormat("hh:mm:ss.S");
    private SimpleDateFormat sdfTopClock = new SimpleDateFormat("hh:mm");

    public ClockApp(ArrowImageView arrowSeconds, ArrowImageView arrowMinutes, ArrowImageView arrowHours, MusicPlayer musicPlayer) {
        this.arrowSeconds = arrowSeconds;
        this.arrowMinutes = arrowMinutes;
        this.arrowHours = arrowHours;
        this.musicPlayer = musicPlayer;
        setListeners();
        initRotations();
    }

    public void rotateAnalogClock() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        arrowHours.setRotation(30 * hour, ARROW_HOUR);
        arrowMinutes.setRotation(6 * minute, ARROW_MIN);
        arrowSeconds.setRotation(6 * seconds);
    }

    private void setListeners() {
        arrowHours.setListener(this);
        arrowMinutes.setListener(this);

    }

    private void initRotations() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        arrowHours.setRotation(30 * hour);
        arrowMinutes.setRotation(6 * minute);
        arrowSeconds.setRotation(6 * seconds);
    }

    public String setTimeTopClock() {
        return sdfTopClock.format(System.currentTimeMillis());
    }

    public String setTimeBottomClock() {
        return sdfBottomClock.format(System.currentTimeMillis());
    }
//TODO Срабатывает и часовая и минутная музыка
    @Override
    public void rotationChanged(float rotation, int arrowIndex) {
        switch (arrowIndex){
            case ARROW_HOUR:
                musicPlayer.playMusic(MusicPlayer.MUSIC_HOUR, true);
                break;
            case ARROW_MIN:
                if (rotation == 90 || rotation == 180 || rotation == 270) {
                    musicPlayer.playMusic(MusicPlayer.MUSIC_15_MIN, false);
                } else {
                    musicPlayer.playMusic(MusicPlayer.MUSIC_MIN, false);
                }
        }
    }
}
