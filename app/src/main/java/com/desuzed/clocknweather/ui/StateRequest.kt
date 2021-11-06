package com.desuzed.clocknweather.ui


sealed class StateRequest {
    class Loading(var query: String = "") : StateRequest() {
        fun hasQuery(): Boolean {
            return query.isNotEmpty()
        }
    }

    class Success(var successData: String = "") : StateRequest() {
        fun hasData () :Boolean{
            return successData.isNotEmpty()
        }
    }

    class Error(val message: String) : StateRequest()

    class NoData : StateRequest()
}