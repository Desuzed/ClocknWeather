package com.desuzed.everyweather.presentation.features.main_activity

sealed interface MainActivityAction {
    class ChangeLanguage(val lang: String) : MainActivityAction
}