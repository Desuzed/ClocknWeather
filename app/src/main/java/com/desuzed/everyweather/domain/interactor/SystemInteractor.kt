package com.desuzed.everyweather.domain.interactor

import com.desuzed.everyweather.data.repository.providers.UserLocationProvider
import com.desuzed.everyweather.domain.repository.local.SharedPrefsProvider
import com.desuzed.everyweather.util.NetworkConnection

class SystemInteractor(
    private val networkConnection: NetworkConnection,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val userLocationProvider: UserLocationProvider,
) {
    fun hasInternetFlow() = networkConnection.hasInternetFlow()

    fun userLocationFlow() = userLocationProvider.userLocationFlow

    fun arePermissionsGranted() = userLocationProvider.arePermissionsGranted()

    fun findUserLocation() = userLocationProvider.findUserLocation()

    fun isFirstRunApp() = sharedPrefsProvider.isFirstRunApp()

    fun cancelLookingForLocation() = userLocationProvider.onCancel()

}