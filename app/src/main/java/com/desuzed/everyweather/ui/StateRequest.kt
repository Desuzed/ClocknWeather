package com.desuzed.everyweather.ui


sealed class StateRequest {
    class Loading() : StateRequest() {
//        fun hasQuery(): Boolean {
//            return query.isNotEmpty()
//        }
    }

    class Success(var toggleSaveButton: Boolean = false) : StateRequest() {
//        fun hasData () :Boolean{
//            return toggleSaveButton.isNotEmpty()
//        }
    }

    class Error(val message: String) : StateRequest()

    class NoData : StateRequest()
}