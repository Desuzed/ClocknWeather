package com.desuzed.everyweather.mvvm.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App

class AppViewModelFactory(private val application: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(NetworkViewModel::class.java) -> {
//                NetworkViewModel(application.repositoryApp) as T
//            }
//            modelClass.isAssignableFrom(LocationViewModel::class.java) -> {
//                LocationViewModel(application.repositoryApp) as T
//            }
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(application.repositoryApp) as T
            }

            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}
