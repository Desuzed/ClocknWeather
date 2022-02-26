package com.desuzed.everyweather.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.data.repository.RepositoryApp

/**
 * Factory for creating ViewModels with parameters
 */
class AppViewModelFactory(private val repositoryApp: RepositoryApp) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(repositoryApp) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}
