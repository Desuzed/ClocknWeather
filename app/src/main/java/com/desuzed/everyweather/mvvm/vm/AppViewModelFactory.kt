package com.desuzed.everyweather.mvvm.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App

/**
 * Factory for creating ViewModels with parameters
 */
class AppViewModelFactory(private val application: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(application.repositoryApp) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}
