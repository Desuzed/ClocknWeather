package com.desuzed.everyweather.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.desuzed.everyweather.R
import com.desuzed.everyweather.domain.model.UserLatLng
import com.desuzed.everyweather.domain.model.UserLatLngMapper
import com.desuzed.everyweather.presentation.features.main_activity.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHandler(
    private val activity: Activity,//todo koin
    private val viewModel: MainActivityViewModel
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)


    fun findUserLocation() {
        if (viewModel.userLatLng.value?.let { shouldRefreshUserLocation(it) } == false) {
            viewModel.toggleLookingForLocation(false)
            return
        }

        if (permissionsGranted()) {
            viewModel.toggleLookingForLocation(true)
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                null
            )
                .addOnSuccessListener {
                    if (it != null) {
                        Log.i("TAG", "findUserLocation: found: ${it.latitude}")
                        val userLatLng = UserLatLngMapper().mapFromEntity(it)
                        viewModel.userLatLng.value = userLatLng
                        viewModel.toggleLookingForLocation(false)
                    } else {
                        Log.i("TAG", "findUserLocation: NOT FOUND")
                        onError(activity.resources.getString(R.string.your_current_location_not_found))
                    }
                }
        } else {
            onError(activity.resources.getString(R.string.location_permissions_are_not_granted))
        }
    }

    private fun shouldRefreshUserLocation(userLatLng: UserLatLng): Boolean =
        System.currentTimeMillis() - userLatLng.time > 1000 * 60

    private fun onError(message: String) {
        viewModel.toggleLookingForLocation(false)
        viewModel.postMessage(message)
    }

    fun permissionsGranted(): Boolean {
        val permissionFine = ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permissionCoarse = ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionFine == PackageManager.PERMISSION_GRANTED
                || permissionCoarse == PackageManager.PERMISSION_GRANTED
    }


//        val request = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }

//        fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val lastLocation: Location? = locationResult.lastLocation
//                Log.i(
//                    "TAG",
//                    "getCurrentLocation: lat: ${lastLocation.latitude} ; lon: ${lastLocation.longitude}"
//                )
//                if (lastLocation != null) {
//                    val latLon = LatLon(lastLocation.latitude, lastLocation.longitude)
//                    locationViewModel.postValue(latLon)
//                }
//            }
//        }, null)


}