package com.desuzed.everyweather.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.desuzed.everyweather.R
import com.desuzed.everyweather.model.Event
import com.desuzed.everyweather.model.entity.LocationApp
import com.desuzed.everyweather.model.entity.LocationAppMapper
import com.desuzed.everyweather.view.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHandler(
    private val activity: Activity,
    private val mainActivityViewModel: MainActivityViewModel
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun findUserLocation() {
        if (mainActivityViewModel.locationLiveData.value?.let { shouldRefreshUserLocation(it) } == false) {
            mainActivityViewModel.toggleLookingForLocation.postValue(false)
            return
        }

        if (permissionsGranted()) {
            mainActivityViewModel.toggleLookingForLocation.postValue(true)
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            )
                .addOnSuccessListener {
                    if (it != null) {
                        val locationApp = LocationAppMapper().mapFromEntity(it)
                        mainActivityViewModel.locationLiveData.postValue(locationApp)
                        mainActivityViewModel.toggleLookingForLocation.postValue(false)
                    } else {
                        onError(activity.resources.getString(R.string.your_current_location_not_found))
                    }
                }
        } else {
            onError(activity.resources.getString(R.string.location_permissions_are_not_granted))
        }
    }

    private fun shouldRefreshUserLocation(locationApp: LocationApp): Boolean =
        System.currentTimeMillis() - locationApp.time > 1000 * 60

    private fun onError(message: String) {
        mainActivityViewModel.toggleLookingForLocation.postValue(false)
        mainActivityViewModel.messageLiveData.postValue(Event(message))
    }

    private fun permissionsGranted(): Boolean {
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