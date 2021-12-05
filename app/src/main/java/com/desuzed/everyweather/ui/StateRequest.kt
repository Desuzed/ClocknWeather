package com.desuzed.everyweather.ui


sealed class StateRequest {
    class Success(var toggleSaveButton: Boolean = false) : StateRequest(){
        var message : String = ""
        constructor(successMessage : String):this(){
            this.message = successMessage
        }
    }
    class Loading : StateRequest()
    class Error(val message: String) : StateRequest()
    class NoData : StateRequest()
}