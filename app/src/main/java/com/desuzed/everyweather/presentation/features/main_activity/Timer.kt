package com.desuzed.everyweather.presentation.features.main_activity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Timer(private val scope: CoroutineScope) {
    private val _timerFlow = MutableStateFlow<Long>(0)

    private val job = scope.launch {
        _timerFlow.collect {

        }
    }

    suspend fun startTimer(seconds: Int) {
        val secondsToEmit = seconds * 1000
        val time = System.currentTimeMillis() + secondsToEmit
        delay(secondsToEmit.toLong())
        _timerFlow.emit(time)
    }

    fun stopTimer() {
        job.cancel()
    }
}