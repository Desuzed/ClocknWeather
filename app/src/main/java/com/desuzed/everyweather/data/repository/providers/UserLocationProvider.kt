package com.desuzed.everyweather.data.repository.providers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.desuzed.everyweather.data.mapper.location.UserLatLngMapper
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider.Companion.LOCATION_NOT_FOUND
import com.desuzed.everyweather.data.repository.providers.action_result.GeoActionResultProvider.Companion.NO_LOCATION_PERMISSIONS
import com.desuzed.everyweather.domain.model.location.UserLatLng
import com.desuzed.everyweather.domain.model.location.UserLocationResult
import com.desuzed.everyweather.domain.model.result.ActionResult
import com.desuzed.everyweather.domain.model.result.ActionType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserLocationProvider(
    private val context: Context,
    private val userLatLngMapper: UserLatLngMapper,
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var lookingForLocation: Task<Location?>? = null

    private val _userLocationFlow = MutableStateFlow<UserLocationResult?>(null)
    val userLocationFlow: Flow<UserLocationResult?> = _userLocationFlow.asStateFlow()

    private var cancellationTokenSource: CancellationTokenSource? = null

    fun findUserLocation(): Boolean {
        val oldValue = _userLocationFlow.value?.userLatLng
        val shouldRefreshLocation = oldValue?.let { shouldRefreshUserLocation(it) }
        if (shouldRefreshLocation == false || lookingForLocation != null) {
            val newValue = UserLocationResult(userLatLng = oldValue)
            _userLocationFlow.value = newValue
            return false
        }
        if (arePermissionsGranted()) {
            val newCancellationSource = CancellationTokenSource()
            cancellationTokenSource = newCancellationSource
            lookingForLocation = fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                newCancellationSource.token
            )
                .addOnCanceledListener {
                }
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onSuccess(location)
                    } else {
                        onNull()
                    }
                    stopLookingForLocation()
                }
            return true
        } else {
            val actionError = ActionResult(
                code = NO_LOCATION_PERMISSIONS,
                actionType = ActionType.OK
            )
            _userLocationFlow.value = UserLocationResult(actionResult = actionError)
            stopLookingForLocation()
            return false
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

    fun onCancel() {
        stopLookingForLocation()
        onNull()
    }

    private fun stopLookingForLocation() {
        cancellationTokenSource?.cancel()
        cancellationTokenSource = null
        lookingForLocation = null
    }

    private fun onSuccess(location: Location) {
        val result = userLatLngMapper.mapFromEntity(location)
        _userLocationFlow.value = UserLocationResult(userLatLng = result)
    }

    private fun onNull() {
        val actionError = ActionResult(
            code = LOCATION_NOT_FOUND,
            actionType = ActionType.RETRY
        )
        _userLocationFlow.value = UserLocationResult(actionResult = actionError)
    }

    private fun shouldRefreshUserLocation(userLatLng: UserLatLng): Boolean =
        System.currentTimeMillis() - userLatLng.time > SIXTY_SECONDS

    companion object {
        private const val SIXTY_SECONDS = 60_000
        private const val GRANTED = PackageManager.PERMISSION_GRANTED
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

}