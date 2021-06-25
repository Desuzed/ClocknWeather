package com.desuzed.clocknweather.util;

import com.desuzed.clocknweather.mvvm.ClockViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClockApp implements ArrowImageView.OnRotationChangedListener {
    public static final int ARROW_MIN = 10;
    public static final int ARROW_HOUR = 20;
    private final ArrowImageView arrowMinutes, arrowHours, arrowSeconds;
    private final MusicPlayer musicPlayer;
    private final SimpleDateFormat sdfBottomClock = new SimpleDateFormat("hh:mm:ss.S");
    private final SimpleDateFormat sdfTopClock = new SimpleDateFormat("hh:mm");
    private ClockViewModel viewModel;

    public ClockApp(ArrowImageView arrowSeconds, ArrowImageView arrowMinutes, ArrowImageView arrowHours, MusicPlayer musicPlayer, ClockViewModel viewModel) {
        this.arrowSeconds = arrowSeconds;
        this.arrowMinutes = arrowMinutes;
        this.arrowHours = arrowHours;
        this.musicPlayer = musicPlayer;
        this.viewModel = viewModel;
        setListeners();
        initRotations();
    }

    public void rotateAnalogClock() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        arrowHours.setRotation(30 * hour, ARROW_HOUR);
        arrowSeconds.setRotation(6 * seconds);
        arrowMinutes.setRotation(6 * minute, ARROW_MIN);


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
        // todo - это надо бить на неск.методов - получение секунд, минут и часов тек.времени,
        //  они пригодятся, надо переиспользовать.
        //  или рядом с часовым фрагментом лучше/ его ВМ, его никто больше не использует.
        //  Фрагменты  (и активности) в ui пакете, если фрагментов много - их суют в отд.пакет,
        //   как и ВМ тоже. Но это если много.
        arrowHours.setRotation(30 * hour);
        arrowMinutes.setRotation(6 * minute);
        arrowSeconds.setRotation(6 * seconds);
        //  todo  поворот стрелки логичнее держать во фрагменте. Типа turnHourArrow(value)
        //   а данные придут в лайфдату из ВМ готовые. То же с часами - придет готовый текст
        //   кто сильно умный может прислать один текст для всех и для минутных обрезать
        //   лишнее с конца уже во фрагменте, что бы не плодить обсерверы
    }

    public String setTimeTopClock() {
        return sdfTopClock.format(System.currentTimeMillis());
    }

    public String setTimeBottomClock() {
        return sdfBottomClock.format(System.currentTimeMillis());
    }

    @Override
    public void rotationChanged(float rotation, int arrowIndex) {
        switch (arrowIndex){
            case ARROW_HOUR:
                musicPlayer.playHourMusic();
                break;
            case ARROW_MIN:
                if (rotation == 90 || rotation == 180 || rotation == 270  || rotation == 0) {
                    musicPlayer.play15MinMusic();
                } else {
                    musicPlayer.playMinMusic();
                }
                MusicPlayer.ONLY_HOUR_MUSIC=false;
                break;
        }
    }
}
