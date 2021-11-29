package com.desuzed.everyweather.ui


sealed class StateRequest {
    class Loading : StateRequest()
    class Success(var toggleSaveButton: Boolean = false) : StateRequest()
    class Error(val message: String) : StateRequest()
    class NoData : StateRequest()
}