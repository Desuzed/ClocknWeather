package com.desuzed.everyweather.view


sealed class StateUI {
    class Success(var toggleSaveButton: Boolean = false) : StateUI(){
        var message : String = ""
        constructor(successMessage : String):this(){
            this.message = successMessage
        }
    }
    class Loading : StateUI()
    class Error(val message: String) : StateUI()
    class NoData : StateUI()
}