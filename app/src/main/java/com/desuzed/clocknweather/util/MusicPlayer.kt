package com.desuzed.clocknweather.util

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.desuzed.clocknweather.R

class MusicPlayer(private val checkBoxManager: CheckBoxManager, private val context: Context) {
    fun playMinMusic() {
        if (checkBoxManager.checkBoxStates.stateMinute && !ONLY_HOUR_MUSIC) {
            MediaPlayer
                .create(context, R.raw.woody)
                .start()
            Log.i("MusicPlayer", "playMinMusic: ")
        }
    }

    fun playHourMusic() {
        if (checkBoxManager.checkBoxStates.stateHour) {
            MediaPlayer
                .create(context, R.raw.sound2)
                .start()
            Log.i("MusicPlayer", "playHOURMusic: ")
        }
    }

    fun play15MinMusic() {
        if (checkBoxManager.checkBoxStates.state15min && !ONLY_HOUR_MUSIC) {
            MediaPlayer
                .create(context, R.raw.icq)
                .start()
            Log.i("MusicPlayer", "play15MinMusic: ")
        }
    }

    companion object {
        @JvmField
        var ONLY_HOUR_MUSIC = false
    }
}