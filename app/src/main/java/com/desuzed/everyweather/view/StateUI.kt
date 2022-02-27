package com.desuzed.everyweather.view


sealed class StateUI {
    class Success(var message: String? = null, var toggleSaveButton: Boolean = false) : StateUI()
    class Loading : StateUI()
    class Error(val message: String) : StateUI()
    class NoData : StateUI()
}