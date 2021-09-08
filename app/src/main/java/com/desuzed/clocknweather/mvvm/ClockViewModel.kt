package com.desuzed.clocknweather.mvvm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.desuzed.clocknweather.util.MusicPlayer
import com.desuzed.clocknweather.util.TimeGetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ClockViewModel(private val repo: Repository) : ViewModel(){
    val hourLiveData = MutableLiveData<Int>()
    val minLiveData = MutableLiveData<Int>()
    val secLiveData = MutableLiveData<Int>()
    val mSecLiveData = MutableLiveData<Int>()

    val checkBoxLiveData: LiveData<CheckBoxStates>
        get() = repo.checkBoxLiveData
    init {
        hourLiveData.value = TimeGetter().hour
        minLiveData.value = TimeGetter().minute
        secLiveData.value = TimeGetter().sec
        mSecLiveData.value = TimeGetter().mSec
    }

    fun flowEmitter () : Flow<Long> = flow {
        var counter : Long = 0
        while (true){
            delay(100)
            emit(counter++)
        }
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