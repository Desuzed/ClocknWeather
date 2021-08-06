package com.desuzed.clocknweather.mvvm

class CheckBoxStates {
    var stateMinute = false
    var state15min = false
    var stateHour = false

    constructor()
    constructor(stateMinute: Boolean, state15min: Boolean, stateHour: Boolean) {
        this.stateMinute = stateMinute
        this.state15min = state15min
        this.stateHour = stateHour
    }
}