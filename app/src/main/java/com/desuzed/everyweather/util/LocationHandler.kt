package com.desuzed.everyweather.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.desuzed.everyweather.R
import com.desuzed.everyweather.mvvm.LocationApp
import com.desuzed.everyweather.mvvm.vm.SharedViewModel
import com.desuzed.everyweather.ui.StateRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHandler(
    private val activity: Activity,
    private val sharedViewModel: SharedViewModel
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

//TODO refactor
    fun postCurrentLocation() {
    sharedViewModel.stateLiveData.postValue(StateRequest.Loading())
        if (permissionsGranted()) {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener {
                    sharedViewModel.stateLiveData.postValue(StateRequest.Loading())
                    if (it != null) {
                        val locationApp = LocationApp (it.latitude.toFloat(), it.longitude.toFloat())
                        sharedViewModel.postLocation(locationApp)
                    }else{
                        onError("Проверьте настройки местоположения")
                    }

                }
        } else {
           postLastLocation()
        }
    }

    fun postLastLocation() {
        if (permissionsGranted()) {
            sharedViewModel.stateLiveData.postValue(StateRequest.Loading())
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    val locationApp = LocationApp (it.latitude.toFloat(), it.longitude.toFloat())
                 //   val latLon = LatLon(it.latitude, it.longitude)
                    sharedViewModel.postLocation(locationApp)
                }
            }
        } else {
            onError(activity.resources.getString(R.string.location_permissions_are_not_granted))
        }
    }

    fun onError(message: String) {
        sharedViewModel.stateLiveData.postValue(StateRequest.Error(message))
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