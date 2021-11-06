package com.desuzed.clocknweather.network.model

data class Query(val text : String, var userInput : Boolean = false ) {
    fun isEmpty () : Boolean{
        return text.isEmpty()
    }
}