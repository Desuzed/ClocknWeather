package com.desuzed.everyweather.presentation.ui.base

//TODO вынести в обычный дата класс без наследников т.к получился оверинжениринг
abstract class DetailCard {
    abstract val wind: String
    abstract val pressure: String
    abstract val humidity: String
    abstract val pop: String
    abstract val sun: String
    abstract val moon: String
}