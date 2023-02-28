package com.desuzed.everyweather.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

object Timer {

    fun timerFlow(seconds: Int) = flow {
        var tick = 0
        while (tick < seconds) {
            emit(++tick)
            delay(Constants.SECONDS_MULTIPLIER)
        }
    }
}