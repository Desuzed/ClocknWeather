package com.desuzed.everyweather.data.repository.providers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.desuzed.everyweather.data.repository.providers.action_result.ActionResult
import com.desuzed.everyweather.data.repository.providers.action_result.ActionType
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider.Companion.LOCATION_NOT_FOUND
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider.Companion.NO_LOCATION_PERMISSIONS
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.domain.model.UserLatLngMapper
import com.desuzed.everyweather.domain.model.location.UserLocationResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserLocationProvider(
    private val context: Context,
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var lookingForLocation: Task<Location?>? = null

    private val _userLocationFlow = MutableStateFlow<UserLocationResult?>(null)
    val userLocationFlow: Flow<UserLocationResult?> = _userLocationFlow.asStateFlow()

    fun findUserLocation() {
        val shouldRefreshLocation =
            _userLocationFlow.value?.userLatLng?.let { shouldRefreshUserLocation(it) }
        if (shouldRefreshLocation == false || lookingForLocation != null) {
            return
        }
        if (arePermissionsGranted()) {
            lookingForLocation = fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                null
            )
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val result = UserLatLngMapper().mapFromEntity(location)
                        _userLocationFlow.value = UserLocationResult(userLatLng = result)
                        lookingForLocation = null
                    } else {
                        val actionError = ActionResult(
                            code = LOCATION_NOT_FOUND,
                            actionType = ActionType.RETRY
                        )
                        _userLocationFlow.value = UserLocationResult(actionResult = actionError)
                        lookingForLocation = null
                    }
                }
        } else {
            val actionError = ActionResult(
                code = NO_LOCATION_PERMISSIONS,
                actionType = ActionType.OK
            )
            _userLocationFlow.value = UserLocationResult(actionResult = actionError)
            lookingForLocation = null
        }
    }

    fun arePermissionsGranted(): Boolean {
        val permissionFine = ContextCompat.checkSelfPermission(
            context,
            FINE_LOCATION
        )

        val permissionCoarse = ContextCompat.checkSelfPermission(
            context,
            COARSE_LOCATION
        )
        return permissionFine == GRANTED || permissionCoarse == GRANTED
    }

    private fun shouldRefreshUserLocation(userLatLng: UserLatLng): Boolean =
        System.currentTimeMillis() - userLatLng.time > 1000 * 60

    companion object {
        private const val GRANTED = PackageManager.PERMISSION_GRANTED
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

}