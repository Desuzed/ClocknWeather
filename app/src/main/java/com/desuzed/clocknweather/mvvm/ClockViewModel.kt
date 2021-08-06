package com.desuzed.clocknweather.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.desuzed.clocknweather.rx.HourObserver
import com.desuzed.clocknweather.rx.MSecObserver
import com.desuzed.clocknweather.rx.MinuteObserver
import com.desuzed.clocknweather.rx.SecObserver
import com.desuzed.clocknweather.util.MusicPlayer
import com.desuzed.clocknweather.util.TimeGetter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Predicate
import java.util.concurrent.TimeUnit

class ClockViewModel(app: Application?) : AndroidViewModel(app!!) {
    private val repo: Repository = Repository(app!!)
    val emitter: Observable<Long> = Observable.interval(100, TimeUnit.MILLISECONDS)
    private val filterSeconds: Predicate<Long> = Predicate { aLong: Long -> aLong % 10 == 0L }
    val hourLiveData = MutableLiveData<Int>()
    val minLiveData = MutableLiveData<Int>()
    val secLiveData = MutableLiveData<Int>()
    val mSecLiveData = MutableLiveData<Int>()
    val hourObserver = HourObserver(hourLiveData)
    val minuteObserver = MinuteObserver(minLiveData)
    val secObserver = SecObserver(secLiveData)
    val mSecObserver = MSecObserver(mSecLiveData)
    val checkBoxLiveData: LiveData<CheckBoxStates>
        get() = repo.checkBoxLiveData
    init {
        hourLiveData.value = TimeGetter().hour
        minLiveData.value = TimeGetter().minute
        secLiveData.value = TimeGetter().sec
        mSecLiveData.value = TimeGetter().mSec

    }

    fun playMusic(rotation: Float, arrowIndex: Int, mp: MusicPlayer) {
        when (arrowIndex) {
            ARROW_HOUR -> mp.playHourMusic()
            ARROW_MIN -> {
                if (rotation == 90f || rotation == 180f || rotation == 270f || rotation == 0f) {
                    mp.play15MinMusic()
                } else {
                    mp.playMinMusic()
                }
                MusicPlayer.ONLY_HOUR_MUSIC = false
            }
        }
    }

    fun setState(checkBoxStates: CheckBoxStates?) {
        repo.setState(checkBoxStates!!)
    }

    companion object {
        const val ARROW_MIN = 10
        const val ARROW_HOUR = 20
    }

}