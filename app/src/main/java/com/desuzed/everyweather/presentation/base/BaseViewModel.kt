package com.desuzed.everyweather.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : State, E : SideEffect, A : UserInteraction>(
    initState: S,
) : ViewModel() {
    private val _state = MutableStateFlow(initState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<E>()
    val sideEffect: SharedFlow<E> = _sideEffect.asSharedFlow()

    protected fun setState(reducer: S.() -> S) {
        _state.value = reducer(_state.value)
    }

    protected fun setSideEffect(effect: E) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    open fun onUserInteraction(interaction: A) {}

    inline fun <T> collect(source: Flow<T>, crossinline consumer: suspend (T) -> Unit) {
        viewModelScope.launch {
            source.collect {
                consumer(it)
            }
        }
    }
}