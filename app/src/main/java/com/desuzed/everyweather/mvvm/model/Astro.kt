package com.desuzed.everyweather.mvvm.model


class Astro(
//TODO переделать чтобы строка приходила не с AM/PM , а в 24-часовом формате
    val sunrise: String,

    val sunset: String,

    val moonrise: String,

    val moonset: String
)