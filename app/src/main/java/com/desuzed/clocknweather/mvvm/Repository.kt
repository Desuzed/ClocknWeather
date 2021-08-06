package com.desuzed.clocknweather.mvvm

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Repository(val app: Application) {
    val checkBoxLiveData = MutableLiveData<CheckBoxStates> ()

    init {
        val stateMin = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE).getBoolean(
            STATE_MIN, false
        )
        val state15Min = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE).getBoolean(
            STATE_15_MIN, false
        )
        val stateHour = app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE).getBoolean(
            STATE_HOUR, false
        )
        checkBoxLiveData.value = CheckBoxStates(stateMin, state15Min, stateHour)
    }

    fun setState(checkBoxStates: CheckBoxStates) {
        checkBoxLiveData.value = checkBoxStates
        app.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(STATE_MIN, checkBoxStates.stateMinute)
            .putBoolean(STATE_15_MIN, checkBoxStates.state15min)
            .putBoolean(STATE_HOUR, checkBoxStates.stateHour)
            .apply()
    }

    companion object {
        const val STATE_MIN = "STATE_MIN"
        const val STATE_15_MIN = "STATE_15_MIN"
        const val STATE_HOUR = "STATE_HOUR"
        const val S_PREF_NAME = "SP"
    }
}